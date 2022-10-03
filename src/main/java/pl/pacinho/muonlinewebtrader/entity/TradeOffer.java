package pl.pacinho.muonlinewebtrader.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}