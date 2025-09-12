package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Entity.CurrencyEntity;
import org.example.Utils.Validator;
import org.example.repository.CurrencyRepository;
import org.example.repository.CurrencyRepositoryImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {


    private final ObjectMapper mapper = new ObjectMapper();
    private final CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyEntity> currencies = currencyRepository.selectAll();
        mapper.writeValue(resp.getOutputStream(), currencies);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        CurrencyEntity currency = mapper.readValue(req.getInputStream(), CurrencyEntity.class);
        CurrencyEntity insertedCurrency = currencyRepository.save(currency);

        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), insertedCurrency);
    }

    @Override
    protected void  doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
String id = req.getParameter("id");
currencyRepository.delete(Long.parseLong(id));
resp.sendRedirect( req.getContextPath() +"/currencies");
    }
}
