package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class TradeOfferDto {

    private Long id;
    private String senderName;
    private String receiverName;
    private LocalDateTime offerDate;
    private TradeOfferStatus status;
}