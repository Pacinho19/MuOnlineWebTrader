package pl.pacinho.muonlinewebtrader.tools;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerialCodeUtils {
    private static final String SIGNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String getSerialNumber() {
        return IntStream.range(0, CODE_LENGTH)
                .mapToObj(i -> String.valueOf(SIGNS.charAt(SECURE_RANDOM.nextInt(SIGNS.length()))))
                .collect(Collectors.joining());
    }
}