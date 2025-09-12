package org.example.repository;

import org.example.Entity.CurrencyEntity;
import org.example.Entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRepository extends CrudRepository<ExchangeRate,Long> {
    Optional<ExchangeRate> findByCodes(String  baseCurCode, String targetCurCode);
    void updateRate(ExchangeRate exchangeRate);
}
