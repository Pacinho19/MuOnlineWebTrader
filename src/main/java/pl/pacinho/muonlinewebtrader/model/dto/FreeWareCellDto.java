package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
public class FreeWareCellDto extends WareCellDto {

    private String icon;
    public static FreeWareCellDto createFreeCell(Integer rowNumber, Integer colNumber, int cellNumber) {
        return FreeWareCellDto.builder()
                .rowNumber(rowNumber)
                .colNumber(colNumber)
                .number(cellNumber)
                .type(CellType.FREE)
                .icon(ImageUtils.encodeFileToBase64Binary(FileUtils.IMG_LOCATION+"/wareCell.png"))
                .build();
    }
}