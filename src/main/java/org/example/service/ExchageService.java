package org.example.service;

import org.example.Entity.ExchangeRate;
import org.example.exception.NotFoundException;
import org.example.repository.ExchangeRateRepositoryImpl;
import org.example.repository.ExchangeRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchageService {
    private final ExchangeRepository exchangeRepository = new ExchangeRateRepositoryImpl();
public BigDecimal getExchangeRate(String from, String to, BigDecimal amount) {
    if (from.equals(to)) {
        return amount;
    }
    BigDecimal rate = findCourse(from, to);
    if(rate != null) {
        return amount.multiply(rate);
    }
rate = findReversCurse(from,to);
 if (rate != null) {
     return amount.divide(rate  , RoundingMode.HALF_UP);
}
rate= findCrossCourse(to, from);
return amount.multiply(rate);

}
private BigDecimal findCourse(String from, String to) {
    Optional<ExchangeRate>exchangeRate =exchangeRepository.findByCodes(from, to);
return (!exchangeRate.isEmpty()) ?exchangeRate.get().getRate():null;

}
private BigDecimal findReversCurse(String from, String to) {
    Optional<ExchangeRate>exchangeRate =exchangeRepository.findByCodes(to, from);
    return (!exchangeRate.isEmpty()) ?exchangeRate.get().getRate():null;
}
private BigDecimal findCrossCourse(String from, String to) {
        String commonCurrency = "USD";
        Optional<ExchangeRate> fromExchange = exchangeRepository.findByCodes(commonCurrency, from);
        Optional<ExchangeRate> toExchange = exchangeRepository.findByCodes(commonCurrency, to);

        if (fromExchange.isEmpty() || toExchange.isEmpty()) {
            throw new NotFoundException(String.format("Exchange rate '%s' - '%s' not found in the database", from, to));
        }

        return toExchange.get().getRate().divide(fromExchange.get().getRate(), 3, RoundingMode.HALF_UP);
    }
}
