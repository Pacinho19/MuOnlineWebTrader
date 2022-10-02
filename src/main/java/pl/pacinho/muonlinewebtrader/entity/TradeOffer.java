package pl.pacinho.muonlinewebtrader.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeOffer {

    @Id
    @GenericGenerator(name = "tradeOfferIdGen", strategy = "increment")
    @GeneratedValue(generator = "tradeOfferIdGen")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Account sellerAccount;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Account buyerAccount;

    private LocalDateTime offerDate;
    private TradeOfferStatus status;
}