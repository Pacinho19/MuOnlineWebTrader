package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class PaymentItemsDto {
    private Long blessCount;
    private Long soulCount;
    private Long zenAmount;
}