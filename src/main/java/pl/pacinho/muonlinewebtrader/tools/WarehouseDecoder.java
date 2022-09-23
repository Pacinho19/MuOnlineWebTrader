package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseDecoder {
    private final ItemDecoder itemDecoder;

    public List<ExtendedItemDto> decode(String wareContent) {
        if (wareContent.startsWith("0x")) wareContent = wareContent.substring(2);

        final String wareContentF = wareContent;
        String[] content = wareContentF.split("(?<=\\G.{" + CodeUtils.ITEM_CHUNK_SIZE + "})");
        return IntStream.range(0, content.length)
                .boxed()
                .map(i -> itemDecoder.decode(content[i], i * CodeUtils.ITEM_CHUNK_SIZE))
                .filter(Objects::nonNull)
                .toList();

    }

    public List<ExtendedItemDto> decodeWebItems(List<WebWarehouse> warehouseByAccountName) {
        return warehouseByAccountName
                .stream()
                .map(ww -> itemDecoder.decode(ww.getItem(), -1))
                .filter(Objects::nonNull)
                .toList();
    }
}