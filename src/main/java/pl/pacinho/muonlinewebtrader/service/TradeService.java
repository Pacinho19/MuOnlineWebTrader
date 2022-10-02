package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.model.dto.TradeOfferDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.TradeOfferDtoMapper;
import pl.pacinho.muonlinewebtrader.repository.TradeRepository;

@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final AccountService accountService;

    public void save(TradeOfferDto tradeOfferDto) {
        Account buyer = accountService.findByLogin(tradeOfferDto.getBuyerName());
        if (buyer == null)
            throw new IllegalStateException("Target account doesn't exists !");

        tradeRepository.save(
                TradeOfferDtoMapper.parse(
                        tradeOfferDto.getContent()
                        , accountService.findByLogin(tradeOfferDto.getSellerName())
                        , buyer
                )
        );
    }
}