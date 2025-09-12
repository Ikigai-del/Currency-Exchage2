package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Entity.CurrencyEntity;
import org.example.Utils.Validator;
import org.example.exception.NotFoundException;
import org.example.repository.CurrencyRepository;
import org.example.repository.CurrencyRepositoryImpl;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replaceFirst("/", "");

        Validator.validateCurrencyCode(code);

        CurrencyEntity currency = currencyRepository.findByCode(code)
                .orElseThrow(() ->  new NotFoundException("Currency with code " + code + "not found"));
resp.setContentType("application/json");
resp.setCharacterEncoding("UTF-8");
objectMapper.writeValue(resp.getOutputStream(), currency);
    }

}
