package pl.pacinho.muonlinewebtrader.utils;

import java.util.Random;

public class RandomUtils {
    public static long randomLong(int min, long max) {
        return new Random().ints(min, Integer.parseInt("" + max))
                .limit(1)
                .boxed()
                .findFirst()
                .get();
    }
}