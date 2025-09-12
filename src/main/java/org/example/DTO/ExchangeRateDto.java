package org.example.DTO;

import java.math.BigDecimal;

public class ExchangeRateDto {
private long id;
private String currencies;
private BigDecimal rate;

    public ExchangeRateDto(long id, String currencies, BigDecimal rate) {
        this.id = id;
        this.currencies = currencies;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
