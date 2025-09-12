package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DTO.ExchangeRateDto;
import org.example.DTO.ExchangeRequestDto;
import org.example.Entity.CurrencyEntity;
import org.example.Entity.ExchangeRate;
import org.example.Utils.Converter;
import org.example.Utils.Validator;
import org.example.exception.NotFoundException;
import org.example.repository.CurrencyRepository;
import org.example.repository.CurrencyRepositoryImpl;
import org.example.repository.ExchangeRateRepositoryImpl;
import org.example.repository.ExchangeRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet  extends HttpServlet {
    private final ExchangeRepository exchangeRepository = new ExchangeRateRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeRepository.selectAll();
        List<ExchangeRateDto> exchangeRateDtos = exchangeRates.stream()
                .map(Converter::convertToDto).collect(Collectors.toList());
        req.setAttribute("exList", exchangeRateDtos);

        List<CurrencyEntity> currencies = currencyRepository.selectAll();
        req.setAttribute("currencies", currencies);

        req.getServletContext().getRequestDispatcher("/exchange.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurCode = req.getParameter("baseCurrency");
        String targetCurCode = req.getParameter("targetCurrency");
        BigDecimal rate = Converter.convertToNumber(req.getParameter("rate").replace(",", "."));
        Validator.validate(new ExchangeRequestDto(baseCurCode, targetCurCode, rate));

        CurrencyEntity base = currencyRepository.findByCode(baseCurCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurCode + "' not found"));
        CurrencyEntity target = currencyRepository.findByCode(targetCurCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurCode + "' not found"));

        exchangeRepository.save(new ExchangeRate(base, target, rate));

        resp.sendRedirect(req.getContextPath() + "/exchangeRates");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        exchangeRepository.delete(id);
        resp.sendRedirect(req.getContextPath() + "/exchangeRates");
    }

}