package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;

public class ItemUtils {

    public static int getItemCountFromBundle(int level) {
        return (level * 10) + 10;
    }

    public static String getItemCode(PaymentItem paymentItem) {
        switch (paymentItem) {
            case BLESS -> {
                return "0D0000" + SerialCodeUtils.getSerialNumber() + "0000E000FFFFFFFFFF";
            }
            case SOUL -> {
                return "0E0000" + SerialCodeUtils.getSerialNumber() + "0000E000FFFFFFFFFF";
            }
        }
        return CodeUtils.EMPTY_CODE;
    }

}