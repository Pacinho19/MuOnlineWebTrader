package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

import java.util.List;

@RequiredArgsConstructor
public enum PaymentMethod {
    ZEN(null, "ZEN", ImageUtils.getItemIcon(PaymentItem.ZEN)),
    BLESS(List.of(PaymentItem.BLESS, PaymentItem.BLESS_BUNDLE), "Jewel of Bless", ImageUtils.getItemIcon(PaymentItem.BLESS)),
    SOUL(List.of(PaymentItem.SOUL, PaymentItem.SOUL_BUNDLE), "Jewel of Soul", ImageUtils.getItemIcon(PaymentItem.SOUL));

    @Getter
    private final List<PaymentItem> paymentItems;
    @Getter
    private final String name;
    @Getter
    private final String icon;

    public PaymentItem getItem(boolean bundle) {
        return getPaymentItems()
                .stream()
                .filter(i -> i.isBundle() == bundle)
                .findFirst()
                .get();
    }
}