package pl.pacinho.muonlinewebtrader.tools;

import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
                                "" + i.getNumber()
                        , Function.identity()));
    }


    public ExtendedItemDto decode(String itemCode, int position) {
        try {
            if (itemCode.replaceAll("F", "").isEmpty()) return null;

            ExtendedItemDto extendedItemDto = ExtendedItemDto.builder()
                    .id(getId(itemCode))
                    .section(getSection(itemCode))
                    .level(getLevel(itemCode))
                    .exc(checkExc(itemCode))
                    .serialNumber(getSerialNumber(itemCode))
                    .luck(getLuck(itemCode))
                    .skill(getSkill(itemCode))
                    .life(getLifeOption(itemCode))
                    .durability(getDurability(itemCode))
                    .code(itemCode)
                    .position(position)
                    .build();

            return createItem(extendedItemDto, itemCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return unknownItem(ExtendedItemDto.builder().build());
    }

    private List<ExcOption> getExcOptions(String itemCode, ExtendedItemDto extendedItemDto) {
        String exc = itemCode.substring(14, 16);
        exc = CodeUtils.baseConvert(exc, 16, 2);
        exc = CodeUtils.addZero(exc, null);

        final String excFinal = exc;
        return IntStream.range(0, 6)
                .boxed()
                .filter(i -> excFinal.charAt(i + 2) == '1')
                .map(i -> ExcOptionParser.parse(i, extendedItemDto))
                .filter(Objects::nonNull)
                .toList();
    }

    private ExtendedItemDto createItem(ExtendedItemDto extendedItemDto, String itemCode) {
        String key = generateKey(extendedItemDto);
        Item itemDict = itemsMap.get(key);
        if (itemDict == null) return unknownItem(extendedItemDto);

        extendedItemDto.setName(itemDict.getName());
        extendedItemDto.setItemType(itemDict.getCategory());
        extendedItemDto.setIcon(ImageUtils.encodeFileToBase64Binary(itemDict.getIconPath()));
        extendedItemDto.setWidth(itemDict.getWidth());
        extendedItemDto.setHeight(itemDict.getHeight());
        extendedItemDto.setCharacterClasses(getCharacterClasses(itemDict));
        if (extendedItemDto.isExc()) extendedItemDto.setExcOptions(getExcOptions(itemCode, extendedItemDto));
        return extendedItemDto;
    }

    private List<CharacterClass> getCharacterClasses(Item itemDict) {
        List<CharacterClass> out = new ArrayList<>();
        if (itemDict.getDk() > 0) out.add(CharacterClass.BK);
        if (itemDict.getDw() > 0) out.add(CharacterClass.SM);
        if (itemDict.getDl() > 0) out.add(CharacterClass.DL);
        if (itemDict.getFe() > 0) out.add(CharacterClass.ELF);
        if (itemDict.getMg() > 0) out.add(CharacterClass.MG);
        if (itemDict.getSu() > 0) out.add(CharacterClass.SUM);
        if (itemDict.getRf() > 0) out.add(CharacterClass.RF);
        return out;
    }

    private ExtendedItemDto unknownItem(ExtendedItemDto extendedItemDto) {
        extendedItemDto.setName("Unknown");
        extendedItemDto.setItemType(ItemType.UNKNOWN);
        extendedItemDto.setWidth(1);
        extendedItemDto.setHeight(1);
        extendedItemDto.setIcon(ImageUtils.encodeFileToBase64Binary(ImageUtils.UNKNOWN_IMAGE));
        return extendedItemDto;
    }

    private String generateKey(ExtendedItemDto extendedItemDto) {
        Item item = items.stream()
                .filter(i -> i.getNumber() == extendedItemDto.getNumber())
                .findFirst()
                .orElse(null);

        if (item == null) return null;

        return "" + extendedItemDto.getNumber();
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

    private String getSecondByteBinary(String itemCode) {
        String s = itemCode.substring(2, 4);
        s = CodeUtils.baseConvert(s, 16, 2);
        s = CodeUtils.addZero(s, null);
        return s;
    }
    private String getThirdByteBinary(String itemCode) {
        String s = itemCode.substring(4, 6);
        s = CodeUtils.baseConvert(s, 16, 2);
        s = CodeUtils.addZero(s, null);
        return s;
    }

    private int getLevel(String itemCode) {
        String lvlS = getSecondByteBinary(itemCode).substring(1, 5);
        return Integer.parseInt(CodeUtils.baseConvert(lvlS, 2, 10));
    }

    private boolean getSkill(String itemCode) {
        return getSecondByteBinary(itemCode).charAt(0) == '1';
    }

    private int getLifeOption(String itemCode) {
        String lifeOption = getSecondByteBinary(itemCode).substring(6);
        return Integer.parseInt(lifeOption) * 4;
    }

    private boolean getLuck(String itemCode) {
        return getSecondByteBinary(itemCode).charAt(5) == '1';
    }

    private int getDurability(String itemCode) {
        String durability = getThirdByteBinary(itemCode);
        return Integer.parseInt(durability, 2);
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