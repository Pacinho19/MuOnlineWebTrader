package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

@Getter
@SuperBuilder(toBuilder = true)
public class PaymentItemsDto {
    private Long blessCount;
    private Long soulCount;
    private Long zenAmount;

    public Long getCountByType(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ZEN -> {
                return zenAmount;
            }
            case BLESS -> {
                return blessCount;
            }
            case SOUL -> {
                return soulCount;
            }
        }
        return null;
    }
}