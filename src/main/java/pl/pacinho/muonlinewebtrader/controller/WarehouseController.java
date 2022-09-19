package pl.pacinho.muonlinewebtrader.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.WarehouseDto;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

import javax.websocket.server.PathParam;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/warehouse")
@RestController
public class WarehouseController {

    private final WarehouseDecoder warehouseDecoder;
    private final ItemDecoder itemDecoder;

    @PostMapping("/decode")
    public List<ItemDto> decode(@RequestBody WarehouseDto warehouse) {
        return warehouseDecoder.decode(warehouse.getContent());
    }

    @GetMapping("/item/{itemCode}/decode")
    public ItemDto decode(@PathVariable("itemCode") String itemCode) {
        return itemDecoder.decode(itemCode);
    }
}