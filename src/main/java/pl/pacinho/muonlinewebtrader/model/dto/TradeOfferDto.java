package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TradeOfferDto {

    private String accountName;
    private String content;
    private List<List<WareCellDto>> cells;
}