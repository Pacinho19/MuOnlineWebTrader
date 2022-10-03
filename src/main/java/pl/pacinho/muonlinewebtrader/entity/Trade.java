//package pl.pacinho.muonlinewebtrader.entity;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//public class Trade {
//
//    @Id
//    @GenericGenerator(name = "tradeOfferIdGen", strategy = "increment")
//    @GeneratedValue(generator = "tradeOfferIdGen")
//    private Long id;
//
//    private List<TradeOffer>
//    private LocalDateTime offerDate;
//    private TradeOfferStatus status;
//}
