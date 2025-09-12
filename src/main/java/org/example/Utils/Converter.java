package org.example.Utils;

import org.example.DTO.ExchangeRateDto;
import org.example.Entity.ExchangeRate;
import org.example.exception.InvalidParametrExeption;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

public class Converter {

    public static ExchangeRateDto convertToDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(exchangeRate.getId(),
                exchangeRate.getBaseCurrency().getCode() +
                        exchangeRate.getTargetCurrency().getCode(),
                exchangeRate.getRate());
    }

    public static BigDecimal convertToNumber(String rate) {

        if (rate == null || rate.isBlank()) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        try {
            return BigDecimal.valueOf(Double.parseDouble(rate));
        }
        catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter rate must be a number");
        }
    }


}
