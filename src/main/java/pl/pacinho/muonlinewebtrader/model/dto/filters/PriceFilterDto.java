package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

@Getter
public class PriceFilterDto {

    private PaymentMethod paymentMethod;
    private Long amountMin;
    private Long amountMax;
}