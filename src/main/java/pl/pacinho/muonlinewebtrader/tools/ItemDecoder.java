package pl.pacinho.muonlinewebtrader.tools;

import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;
import pl.pacinho.muonlinewebtrader.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ItemDecoder {

    private final ItemService itemService;
    private final Map<String, Item> itemsMap;
    private final List<Item> items;

    public ItemDecoder(ItemService itemService) {
        this.itemService = itemService;
        items = itemService.findAll();
        this.itemsMap = items
                .stream()
                .collect(Collectors.toMap(i ->
                                "" + i.getNumber() + (i.getLevel() > 0 ? ("#" + i.getLevel()) : "")
                        , Function.identity()));
    }


    public ItemDto decode(String itemCode) {
        if (itemCode.replaceAll("F", "").isEmpty()) return null;

        ItemDto itemDto = ItemDto.builder()
                .id(getId(itemCode))
                .section(getSection(itemCode))
                .level(getLevel(itemCode))
                .exc(checkExc(itemCode))
                .serialNumber(getSerialNumber(itemCode))
                .build();

        return createItem(itemDto, itemCode, true);
    }

    private List<ExcOption> getExcOptions(String itemCode, ItemDto itemDto) {
        String exc = itemCode.substring(14, 16);
        exc = CodeUtils.baseConvert(exc, 16, 2);
        exc = CodeUtils.addZero(exc, null);

        final String excFinal = exc;
        return IntStream.range(0, 6)
                .boxed()
                .filter(i -> excFinal.charAt(i + 2) == '1')
                .map(i -> ExcOptionParser.parse(i, itemDto))
                .toList();
    }

    private ItemDto createItem(ItemDto itemDto, String itemCode, boolean checkWithLevel) {
        String key = generateKey(itemDto, checkWithLevel);
        Item itemDict = itemsMap.get(key);
        if (itemDict == null && !checkWithLevel) return unknownItem(itemDto);
        else if (itemDict == null && checkWithLevel) return createItem(itemDto, itemCode, false);

        itemDto.setName(itemDict.getName());
        itemDto.setItemType(itemDict.getCategory());
        if (itemDto.isExc()) itemDto.setExcOptions(getExcOptions(itemCode, itemDto));
        return itemDto;
    }

    private ItemDto unknownItem(ItemDto itemDto) {
        itemDto.setName("Unknown");
        itemDto.setItemType(ItemType.UNKNOWN);
        return itemDto;
    }

    private String generateKey(ItemDto itemDto, boolean checkWithLevel) {
        Item item = items.stream()
                .filter(i -> i.getNumber() == itemDto.getNumber())
                .findFirst()
                .orElse(null);

        if (item == null) return null;

        if (checkWithLevel)
            return itemDto.getNumber() + "#" + itemDto.getLevel();
        else
            return "" + itemDto.getNumber();
    }

    private String getSerialNumber(String itemCode) {
        return itemCode.substring(6, 14)
                + (itemCode.length() == 40 ? itemCode.substring(32) : "");
    }

    private boolean checkExc(String itemCode) {
        String exc = itemCode.substring(14, 16);
        exc = CodeUtils.baseConvert(exc, 16, 2);
        exc = CodeUtils.addZero(exc, null);
        if (exc.substring(2, 8).contains("1")) return true;
        return false;
    }


    private int getLevel(String itemCode) {
        String lvlS = itemCode.substring(2, 4);
        lvlS = CodeUtils.baseConvert(lvlS, 16, 2);
        lvlS = CodeUtils.addZero(lvlS, null);
        lvlS = lvlS.substring(1, 5);
        return Integer.parseInt(CodeUtils.baseConvert(lvlS, 2, 10));
    }

    private int getSection(String itemCode) {
        return StringUtils.convertToHexadecimal(itemCode.substring(18, 19));
    }

    private int getId(String itemCode) {
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