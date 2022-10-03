package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Trade;

import java.util.List;
import java.util.UUID;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query(
            value = """
                    SELECT t
                        from Trade t
                        join t.senderOffer.account sender
                        join t.receiverOffer.account receiver
                    WHERE sender.name=:name or receiver.name=:name
                    ORDER BY t.offerDate DESC
                    """
    )
    List<Trade> findAllBySenderOfferAccountName(@Param("name") String name);
}