package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WarehouseDto {

    private List<ExtendedItemDto> items;
    private Long zen;
}