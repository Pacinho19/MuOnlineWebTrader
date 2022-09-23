package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
public class ItemWareCellDto extends WareCellDto {

    private ExtendedItemDto extendedItemDto;
    private List<Integer> cellsIdx;

    public static ItemWareCellDto createItemCell(Integer rowNumber, Integer colNumber, int cellNumber, ExtendedItemDto itemDto) {
        return ItemWareCellDto.builder()
                .rowNumber(rowNumber)
                .colNumber(colNumber)
                .number(cellNumber)
                .type(CellType.ITEM)
                .extendedItemDto(itemDto)
                .cellsIdx(CodeUtils.getCellsIndexesByItem(cellNumber, itemDto.getWidth(), itemDto.getHeight()))
                .build();
    }
}
