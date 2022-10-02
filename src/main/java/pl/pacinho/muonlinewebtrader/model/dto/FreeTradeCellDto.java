package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@SuperBuilder(toBuilder = true)
@Getter
public class FreeTradeCellDto extends WareCellDto {

    private String icon;
    public static FreeTradeCellDto createFreeCell(Integer rowNumber, Integer colNumber, int cellNumber) {
        return FreeTradeCellDto.builder()
                .rowNumber(rowNumber)
                .colNumber(colNumber)
                .number(cellNumber)
                .type(CellType.FREE)
                .icon(ImageUtils.encodeFileToBase64Binary(FileUtils.IMG_LOCATION+"/tradeCell.png"))
                .build();
    }
}