package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.TradeOffer;

@Repository
public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long> {

}