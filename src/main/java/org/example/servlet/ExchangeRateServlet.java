package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DTO.ExchangeRequestDto;
import org.example.Entity.ExchangeRate;
import org.example.Utils.Converter;
import org.example.Utils.Validator;
import org.example.exception.NotFoundException;
import org.example.repository.ExchangeRateRepositoryImpl;
import org.example.repository.ExchangeRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRepository exchangeRateRepository = new ExchangeRateRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().replaceFirst("/", "");

        if (code.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided or provided in an incorrect format");
        }

        String baseCur = code.substring(0, 3);
        String targetCur = code.substring(3);
        Validator.validateCurrencyCode(baseCur);
        Validator.validateCurrencyCode(targetCur);


        ExchangeRate exchangeRate = exchangeRateRepository.findByCodes(baseCur, targetCur)
                .orElseThrow(() -> new NotFoundException("There is no Exchange rate with " + baseCur + targetCur + " codes"));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(resp.getWriter(), exchangeRate);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("currencies");

        if (code.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided or provided in an incorrect format");
        }

        String baseCur = code.substring(0, 3);
        String targetCur = code.substring(3);
        BigDecimal newRateValue = Converter.convertToNumber(req.getParameter("rate").replace(",", "."));

        Validator.validate(new ExchangeRequestDto(baseCur, targetCur, newRateValue));

        ExchangeRate exchangeRate = exchangeRateRepository.findByCodes(baseCur, targetCur)
                .orElseThrow(() -> new NotFoundException("There is no Exchange rate with " + baseCur + targetCur + " codes"));
        exchangeRate.setRate(newRateValue);
        exchangeRateRepository.updateRate(exchangeRate);

        resp.sendRedirect(req.getContextPath() + "/exchangeRates");

    }

}

