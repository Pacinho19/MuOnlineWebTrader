package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class ExtendedItemDto extends SimpleItemDto {

    private String serialNumber;

}