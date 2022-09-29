package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

@Getter
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
}