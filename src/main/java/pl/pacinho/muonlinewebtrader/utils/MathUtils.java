package pl.pacinho.muonlinewebtrader.utils;

import java.math.BigDecimal;

public class MathUtils {

    public static int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }
}
