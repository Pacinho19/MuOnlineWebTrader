package pl.pacinho.muonlinewebtrader.tools;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeUtils {

    public static String baseConvert(final String inputValue, final int fromBase, final int toBase) {
        if (fromBase < 2 || fromBase > 36 || toBase < 2 || toBase > 36) return null;
        return Integer.toString(Integer.parseInt(inputValue, fromBase), toBase);
    }

    public static String addZero(String number, Integer digitLength) {
        if (digitLength == null) digitLength = 8;
        return IntStream.range(0, (digitLength - number.length()))
                .boxed()
                .map(i -> "0")
                .collect(Collectors.joining()) + number;
    }

}