package pl.pacinho.muonlinewebtrader.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.dto.WarehouseRequestDto;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ItemDecoderController {

    private final WarehouseDecoder warehouseDecoder;
    private final ItemDecoder itemDecoder;

    @PostMapping("/warehouse/decode")
    public List<ExtendedItemDto> decode(@RequestBody WarehouseRequestDto warehouse) {
        return warehouseDecoder.decode(warehouse.getContent());
    }

    @PostMapping("/warehouse/decode/extended")
    public List<WareCellDto> decodeExtended(@RequestBody WarehouseRequestDto warehouse) {
        return warehouseDecoder.decodeExtended(warehouse.getContent());
    }

    @GetMapping("/item/{itemCode}/decode")
    public ExtendedItemDto decode(@PathVariable("itemCode") String itemCode) {
        return itemDecoder.decode(itemCode, -1);
    }
}