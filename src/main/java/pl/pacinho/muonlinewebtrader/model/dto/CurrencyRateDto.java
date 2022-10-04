package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CurrencyRateDto {

    private final BigDecimal blessToSoulRate = BigDecimal.valueOf(2L);
    private final BigDecimal soulToBlessRate = BigDecimal.valueOf(0.25);
}
