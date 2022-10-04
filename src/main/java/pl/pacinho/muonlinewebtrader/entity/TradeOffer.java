package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @Setter
    @Column(length = 1024)
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "acc_id")
    private Account account;
}