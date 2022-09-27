package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum PaymentItem {

    BLESS(7181, false),
    BLESS_BUNDLE(6174, true),
    SOUL(7182, false),
    SOUL_BUNDLE(6175, true),
    ZEN(-1, false);

    @Getter
    private final int number;
    @Getter
    private final boolean isBundle;

    public static boolean checkNumber(int number) {
        return Arrays.stream(PaymentItem.values())
                .anyMatch(pi -> pi.getNumber() == number);
    }

    public static PaymentItem fromNumber(int number) {
        return Arrays.stream(PaymentItem.values())
                .filter(pi -> pi.getNumber() == number)
                .findFirst()
                .get();
    }
}