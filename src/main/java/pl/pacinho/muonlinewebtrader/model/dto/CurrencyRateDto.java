package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.ExchangeType;

import java.math.BigDecimal;

@Getter
public class CurrencyRateDto {

    private final BigDecimal blessToSoulRate = ExchangeType.BTS.getRate();
    private final BigDecimal soulToBlessRate = ExchangeType.STB.getRate();
}
