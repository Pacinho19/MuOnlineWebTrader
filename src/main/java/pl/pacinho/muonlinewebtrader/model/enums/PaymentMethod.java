package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public enum PaymentMethod {
    ZEN(null, "ZEN"),
    BLESS(List.of(PaymentItem.BLESS, PaymentItem.BLESS_BUNDLE), "Jewel of Bless"),
    SOUL(List.of(PaymentItem.SOUL, PaymentItem.SOUL_BUNDLE), "Jewel of Soul");

    @Getter
    private final List<PaymentItem> paymentItems;
    @Getter
    private final String name;

    public PaymentItem getItem(boolean bundle) {
        return getPaymentItems()
                .stream()
                .filter(i -> i.isBundle() == bundle)
                .findFirst()
                .get();
    }
}