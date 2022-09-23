package pl.pacinho.muonlinewebtrader.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeUtils {

    public static final int ITEM_CHUNK_SIZE = 32;
    public static final Integer WAREHOUSE_ROW_SIZE = 8;
    public static final String EMPTY_CODE = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

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

    public static String addItemToWare(String wareContent, String code) {
        if (wareContent.startsWith("0x")) wareContent = wareContent.substring(2);

        String[] content = wareContent.split("(?<=\\G.{" + CodeUtils.ITEM_CHUNK_SIZE + "})");
        AtomicBoolean replaceFinish = new AtomicBoolean(false);
        return "0x" +
               IntStream.range(0, content.length)
                       .boxed()
                       .map(i -> {
                           if (!replaceFinish.get() && content[i].equals(CodeUtils.EMPTY_CODE)) {
                               replaceFinish.set(true);
                               return code;
                           }
                           return content[i];
                       })
                       .collect(Collectors.joining());
    }

    public static List<Integer> getCellsIndexesByItem(int cellNumber, int width, int height) {
        Set<Integer> out = new HashSet<>();
        out.add(cellNumber);
        if (width > 1)
            out.addAll(IntStream.range(cellNumber, cellNumber + width)
                    .boxed()
                    .toList());

        List<Integer> rowIdx = new ArrayList<>(out);
        if (height > 1)
            rowIdx.forEach(ri->
                    out.addAll(IntStream.range(0, height)
                            .boxed()
                            .map(idx -> ri + (idx * CodeUtils.WAREHOUSE_ROW_SIZE))
                            .toList()));
        return out.stream().toList();
    }
}