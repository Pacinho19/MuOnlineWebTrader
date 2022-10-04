package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.Trade;
import pl.pacinho.muonlinewebtrader.entity.TradeOffer;
import pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus;
import pl.pacinho.muonlinewebtrader.repository.TradeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeOfferService tradeOfferService;

    @Transactional
    public void sendOffer(Account senderAccount, String hexTrade, Account targetAccount) {
        TradeOffer senderOffer = tradeOfferService.addOffer(hexTrade, senderAccount);
        TradeOffer receiverOffer = tradeOfferService.addOffer(null, targetAccount);
        tradeRepository.save(
                Trade.builder()
                        .extendedId(UUID.randomUUID().toString())
                        .senderOffer(senderOffer)
                        .receiverOffer(receiverOffer)
                        .offerDate(LocalDateTime.now())
                        .status(TradeOfferStatus.IN_PROGRESS)
                        .build()
        );
    }

    public List<Trade> findByName(String name) {
        return tradeRepository.findAllBySenderOfferAccountName(name);
    }

    public Optional<Trade> findByAccountAndOfferId(String name, String offerId) {
        return tradeRepository.findOfferById(name, offerId);
    }

    public void update(Trade trade) {
        tradeRepository.save(trade);
    }

    public void updateReceiverOffer(TradeOffer receiverOffer, String hexTrade) {
        receiverOffer.setContent(hexTrade);
        tradeOfferService.update(receiverOffer);
    }
}
