package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;

import java.util.Arrays;
import java.util.List;

public class WarehouseDecoder {

    private static final int CHUNK_SIZE = 32;

    public List<ItemDto> decode(String wareContent){
        return Arrays.stream(wareContent.split("(?<=\\G.{" + CHUNK_SIZE + "})"))
                .map(ItemDecoder::decode)
                .toList();
    }
}