package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

@Setter
public class PriceFilterDto {

    private PaymentMethodFilter paymentMethod;
    private Long amountMin;
    private Long amountMax;

    public PriceFilterDto() {
        this.paymentMethod = PaymentMethodFilter.NONE;
        this.amountMin = 0L;
        this.amountMax = 0L;
    }

    public PaymentMethodFilter getPaymentMethod() {
        if (paymentMethod == null) paymentMethod = PaymentMethodFilter.NONE;
        return paymentMethod;
    }

    public Long getAmountMin() {
        if (amountMin == null) amountMin = 0L;
        return amountMin;

    }

    public Long getAmountMax() {
        if (amountMax == null) amountMax = 0L;
        return amountMax;
    }
}