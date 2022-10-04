package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Trade {

    @Id
    @GenericGenerator(name = "tradeOfferIdGen", strategy = "increment")
    @GeneratedValue(generator = "tradeOfferIdGen")
    private Long id;

    @Column(length=36)
    private String extendedId;

    @OneToOne
    @JoinColumn(name = "send_offer_id")
    private TradeOffer senderOffer;

    @OneToOne
    @JoinColumn(name = "receiver_offer_id")
    private TradeOffer receiverOffer;

    private LocalDateTime offerDate;
    @Enumerated(EnumType.STRING)
    private TradeOfferStatus status;
}
