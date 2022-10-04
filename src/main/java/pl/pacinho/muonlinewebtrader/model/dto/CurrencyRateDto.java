package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CurrencyRateDto {

    private final BigDecimal blessToSoulRate = BigDecimal.valueOf(1.5);
    private final BigDecimal soulToBlessRate = BigDecimal.valueOf(0.25);
}
