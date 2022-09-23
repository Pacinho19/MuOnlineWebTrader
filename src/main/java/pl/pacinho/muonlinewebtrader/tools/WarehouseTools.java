package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    public boolean checkSpaceForPutItem(String code) {
        return true;
    }

    public String addItem(String wareContent, String code) {
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
}
