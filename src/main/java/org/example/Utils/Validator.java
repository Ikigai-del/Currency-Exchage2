package org.example.Utils;

import org.example.DTO.ExchangeRequestDto;
import org.example.Entity.CurrencyEntity;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {
    private static Set<String> currencyCodes;

    public static void validate(CurrencyEntity currencyRequestDto) {
        String code = currencyRequestDto.getCode();
        String name = currencyRequestDto.getFullName();
        String sign = currencyRequestDto.getSign();

        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Missing parameter - code");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Missing parameter - name");
        }

        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Missing parameter - sign");
        }

        validateCurrencyCode(code);
    }

    public static void validate(ExchangeRequestDto exchangeRequestDto) {
        String baseCurrencyCode = exchangeRequestDto.getBaseCurrency();
        String targetCurrencyCode = exchangeRequestDto.getTargetCurrency();
        BigDecimal amount = exchangeRequestDto.getAmount();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - from");
        }

        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - to");
        }

        if (amount == null) {
            throw new InvalidParameterException("Missing parameter - amount");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter - amount must be non-negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    /**
     * Checks if the given currency code is valid based on the available currencies provided by the
     * java.util.{@link Currency#getAvailableCurrencies()} method. The list of valid currency codes is cached for performance
     * optimization, and only retrieved from the Currency class if not already available.
     *
     * @param code The currency code to be checked for validity.
     * @throws InvalidParameterException if the currency code isn't valid.
     */
    public static void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new InvalidParameterException("Currency code must contain exactly 3 letters");
        }

        if (currencyCodes == null) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            currencyCodes = currencies.stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());
        }

        if (!currencyCodes.contains(code)) {
            throw new InvalidParameterException("Currency code must be in ISO 4217 format");
        }
    }
}