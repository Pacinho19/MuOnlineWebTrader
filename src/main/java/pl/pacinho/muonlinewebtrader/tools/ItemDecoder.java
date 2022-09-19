package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;

public class ItemDecoder {
    public static ItemDto decode(String itemCode) {
        if (itemCode.startsWith("0x")) itemCode = itemCode.substring(2);
        return ItemDto.builder()
                .id(getId(itemCode))
                .section(getSection(itemCode))
                .level(getLevel(itemCode))
                .exc(checkExc(itemCode))
                .build();
    }

    private static boolean checkExc(String itemCode) {
        String exc = itemCode.substring(14, 16);
        exc = CodeUtils.baseConvert(exc, 16, 2);
        exc = CodeUtils.addZero(exc, null);
        if (exc.substring(2, 8).contains("1")) return true;
        return false;
    }


    private static int getLevel(String itemCode) {
        String lvlS = itemCode.substring(2, 4);
        lvlS = CodeUtils.baseConvert(lvlS, 16, 2);
        lvlS = CodeUtils.addZero(lvlS, null);
        lvlS = lvlS.substring(1, 5);
        return Integer.parseInt(CodeUtils.baseConvert(lvlS, 2, 10));
    }

    private static int getSection(String itemCode) {
        return StringUtils.convertToHexadecimal(itemCode.substring(18, 19));
    }

    private static int getId(String itemCode) {
        int basicSection = StringUtils.convertToHexadecimal(itemCode.substring(0, 2).toUpperCase());
        String extend = itemCode.substring(14, 15);
        int extendI = StringUtils.convertToHexadecimal(extend);
        extend = CodeUtils.baseConvert("" + extendI, 16, 2);
        extend = StringUtils.str_pad(extend, 4, "0", "STR_PAD_LEFT");
        extend = extend.substring(0, 1);

        return extend.equals("1")
                ? basicSection + 256
                : basicSection;
    }
}