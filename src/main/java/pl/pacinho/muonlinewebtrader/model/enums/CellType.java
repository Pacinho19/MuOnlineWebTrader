package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CellType {

    FREE("_"),
    BLOCKED("X"),
    ITEM("I");

    @Getter
    private final String sign;

}
