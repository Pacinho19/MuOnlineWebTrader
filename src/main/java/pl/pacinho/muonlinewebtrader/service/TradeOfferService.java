package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.TradeOffer;
import pl.pacinho.muonlinewebtrader.repository.TradeOfferRepository;

@RequiredArgsConstructor
@Service
public class TradeOfferService {

    private final TradeOfferRepository tradeOfferRepository;

    public TradeOffer addOffer(String content, Account senderAccount) {
        return tradeOfferRepository.save(
                TradeOffer.builder()
                        .content(content)
                        .account(senderAccount)
                        .build()
        );
    }

    public void update(TradeOffer receiverOffer) {
        tradeOfferRepository.save(receiverOffer);
    }
}