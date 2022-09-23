package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
public class WareCellDto {

    private int number;
    private int rowNumber;
    private int colNumber;
    @Setter
    private CellType type;

    public static WareCellDto createFreeCell(Integer rowNumber, Integer colNumber, int cellNumber) {
        return WareCellDto.builder()
                .rowNumber(rowNumber)
                .colNumber(colNumber)
                .type(CellType.FREE)
                .number(cellNumber)
                .build();
    }
}
