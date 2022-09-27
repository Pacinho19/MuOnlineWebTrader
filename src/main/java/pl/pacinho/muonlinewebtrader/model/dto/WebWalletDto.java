package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.ItemIcon;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@Getter
@AllArgsConstructor
@Builder
public class WebWalletDto {

    private Long blessCount;
    private Long soulCount;
    private Long zenAmount;

    private final String blessIcon = ImageUtils.getItemIcon(ItemIcon.BLESS);
    private final String soulIcon = ImageUtils.getItemIcon(ItemIcon.SOUL);
    private final String zenIcon = ImageUtils.getItemIcon(ItemIcon.ZEN);
}