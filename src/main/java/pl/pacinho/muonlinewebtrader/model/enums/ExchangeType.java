package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum ExchangeType {
    BTS(PaymentMethod.BLESS, PaymentMethod.SOUL, BigDecimal.valueOf(1.5)),
    STB(PaymentMethod.SOUL, PaymentMethod.BLESS, BigDecimal.valueOf(0.25));

    @Getter
    private final PaymentMethod from;
    @Getter
    private final PaymentMethod to;
    @Getter
    private final BigDecimal rate;

    public Long getFromValue(Long value) {
        return -1 * value;
    }

    public Long getTargetValue(Long value) {
        return rate.multiply(BigDecimal.valueOf(value)).longValue();
    }

}
