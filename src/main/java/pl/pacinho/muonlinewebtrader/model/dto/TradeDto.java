package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class TradeDto {

    private String uuid;
    private TradeOfferDto sender;
    private TradeOfferDto receiver;
    private LocalDateTime offerDate;
    private TradeOfferStatus status;
}