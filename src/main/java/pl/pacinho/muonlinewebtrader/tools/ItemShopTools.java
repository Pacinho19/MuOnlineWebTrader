package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;
import pl.pacinho.muonlinewebtrader.entity.WebWallet;
import pl.pacinho.muonlinewebtrader.exceptions.ItemNotFoundException;
import pl.pacinho.muonlinewebtrader.model.dto.*;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemShopDtoMapper;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDescription;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDirection;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionType;
import pl.pacinho.muonlinewebtrader.service.*;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class ItemShopTools {

    private final TransactionService transactionService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final ItemShopService itemShopService;
    private final ItemShopDtoMapper itemShopDtoMapper;
    private final WebWalletService webWalletService;
    private final NotificationService notificationService;
    private final ItemDecoder itemDecoder;
    private final ReentrantLock lock = new ReentrantLock();

    @Transactional
    public void putForSale(String code, PriceDto priceDto, String accountName) throws IllegalStateException {
        validatePrice(priceDto);

        webWarehouseItemService.removeItem(accountName, code);
        itemShopService.addItem(code, priceDto, accountName);

    }

    private void validatePrice(PriceDto priceDto) throws IllegalStateException {
        if (priceDto == null)
            throw new IllegalStateException("Price Settings must be set!");

        if (priceDto.getPriceBless() <= 0
            && priceDto.getPriceSoul() <= 0
            && priceDto.getPriceZen() <= 0)
            throw new IllegalStateException("Minimum one price type mus be set!");
    }

    public ItemShopDto getByCode(String code) {
        ItemShop itemShop = getItemOffer(code);
        return itemShopDtoMapper.parse(itemShop);
    }

    public void incrementItemViewCount(String code) {
        itemShopService.incrementItemViewCount(code);
    }

    @Transactional
    public void buy(String name, String code, PaymentMethod paymentMethod) throws IllegalStateException {
        try {
            lock.lock();
            ItemShop itemOffer = getItemOffer(code);
            Long price = getPriceByPaymentMethod(itemOffer, paymentMethod);
            if (price == 0L)
                throw new IllegalStateException("Cannot buy this item by " + paymentMethod.name());

            if (itemOffer.getSellerAccount().getName().equals(name))
                throw new IllegalStateException("This item is Yours!");

            WebWallet webWallet = webWalletService.findEntityByAccountName(name);
            long walletValue = getWalletValue(webWallet, paymentMethod);
            if (walletValue == -1)
                throw new IllegalStateException("Invalid web wallet state!");

            if (walletValue < price)
                throw new IllegalStateException("You don't have enough " + paymentMethod.getName());

            webWalletService.addToWallet(name, (-1 * price.intValue()), paymentMethod);
            webWalletService.addToWallet(itemOffer.getSellerAccount().getName(), price.intValue(), paymentMethod);
            itemShopService.closeOffer(itemOffer, webWallet.getAccount());
            webWarehouseItemService.addItem(name, itemOffer.getItem());

            ExtendedItemDto item = itemDecoder.decode(itemOffer.getItem(), -1);
            notificationService.add(
                    "Your item " + item.getName() + " was sold for " + price.intValue() + " " + paymentMethod.getName(),
                    itemOffer.getSellerAccount());

            transactionService.addTransaction(
                    itemOffer.getSellerAccount().getName(),
                    new TransactionDto(TransactionDirection.INCOMING,
                            TransactionType.SHOP,
                            paymentMethod,
                            price,
                            LocalDateTime.now(),
                            TransactionDescription.ITEM_SALES.getText() + ": " + item.getName()
                    )
            );

            transactionService.addTransaction(
                    name,
                    new TransactionDto(TransactionDirection.OUTGOING,
                            TransactionType.SHOP,
                            paymentMethod,
                            -1L * price.intValue(),
                            LocalDateTime.now(),
                            TransactionDescription.ITEM_PURCHASE.getText() + ": " + item.getName()
                    )
            );
        } finally {
            lock.unlock();
        }
    }

    private long getWalletValue(WebWallet webWallet, PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ZEN -> {
                return webWallet.getZenAmount();
            }
            case BLESS -> {
                return webWallet.getBlessCount();
            }
            case SOUL -> {
                return webWallet.getSoulCount();
            }
        }

        return -1;
    }

    private Long getPriceByPaymentMethod(ItemShop itemOffer, PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ZEN -> {
                return itemOffer.getZenPrice();
            }
            case BLESS -> {
                return itemOffer.getBlessPrice();
            }
            case SOUL -> {
                return itemOffer.getSoulPrice();
            }
        }
        return 0L;
    }

    private ItemShop getItemOffer(String code) throws ItemNotFoundException {
        Optional<ItemShop> itemOpt = itemShopService.findByCodeAndActive(code, 1);
        if (itemOpt.isEmpty())
            throw new ItemNotFoundException("Not found offers for selected item in shop!");

        return itemOpt.get();
    }

    @Transactional
    public void cancel(String name, String code) throws IllegalStateException {
        ItemShop itemOffer = getItemOffer(code);

        if (!itemOffer.getSellerAccount().getName().equals(name))
            throw new IllegalStateException("This item isn't yours ! What you doing ! The police were informed for this situation.");

        itemShopService.closeOffer(itemOffer, null);
    }
}