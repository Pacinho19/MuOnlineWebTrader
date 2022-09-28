package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemUtils {

    public static int getItemCountFromBundle(int level) {
        return (level * 10) + 10;
    }

    public static String getItemCode(PaymentItem paymentItem, int level) {
        switch (paymentItem) {
            case BLESS -> {
                return "0D0000" + SerialCodeUtils.getSerialNumber() + "0000E000FFFFFFFFFF";
            }
            case BLESS_BUNDLE -> {
                switch (level) {
                    case 1 -> {
                        return "1E0800" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                    case 2 -> {
                        return "1E1000" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                    default -> {
                        return "1E0000" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                }
            }
            case SOUL -> {
                return "0E0000" + SerialCodeUtils.getSerialNumber() + "0000E000FFFFFFFFFF";
            }
            case SOUL_BUNDLE -> {
                switch (level) {
                    case 1 -> {
                        return "1F0800" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                    case 2 -> {
                        return "1F1000" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                    default -> {
                        return "1F0000" + SerialCodeUtils.getSerialNumber() + "0000C000FFFFFFFFFF";
                    }
                }
            }
        }
        return CodeUtils.EMPTY_CODE;
    }

    public static int bundleItemCount(Map<Integer, Integer> bundleMap) {
        AtomicInteger sum = new AtomicInteger(0);
        bundleMap.forEach((level, count) -> {
            sum.addAndGet(count * ItemUtils.getItemCountFromBundle(level));
        });
        return sum.get();
    }

}