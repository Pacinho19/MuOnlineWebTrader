package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PaymentItemsDto {
    private Long blessCount;
    private Long soulCount;
    private Long zenCount;

    public Long getCountByType(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ZEN -> {
                return zenCount;
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