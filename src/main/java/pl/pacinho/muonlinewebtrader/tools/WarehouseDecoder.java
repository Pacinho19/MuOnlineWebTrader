package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class WarehouseDecoder {
    private final ItemDecoder itemDecoder;
    private static final int CHUNK_SIZE = 32;

    public List<ExtendedItemDto> decode(String wareContent){
        if (wareContent.startsWith("0x")) wareContent = wareContent.substring(2);

        return Arrays.stream(wareContent.split("(?<=\\G.{" + CHUNK_SIZE + "})"))
                .map(itemDecoder::decode)
                .filter(Objects::nonNull)
                .toList();
    }
}