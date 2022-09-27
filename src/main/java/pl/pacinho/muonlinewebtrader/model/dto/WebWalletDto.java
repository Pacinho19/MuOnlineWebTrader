package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@Getter
@SuperBuilder(toBuilder = true)
public class WebWalletDto extends PaymentItemsDto{

    private final String blessIcon = ImageUtils.getItemIcon(PaymentItem.BLESS);
    private final String soulIcon = ImageUtils.getItemIcon(PaymentItem.SOUL);
    private final String zenIcon = ImageUtils.getItemIcon(PaymentItem.ZEN);
}