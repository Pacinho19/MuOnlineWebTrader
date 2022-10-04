package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.el.stream.Stream;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.TradeDto;
import pl.pacinho.muonlinewebtrader.model.dto.TradeImages;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.service.*;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.ItemUtils;
import pl.pacinho.muonlinewebtrader.tools.TradeTools;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

import javax.resource.spi.IllegalStateException;
import javax.servlet.http.HttpSession;
import java.util.List;

import static pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus.IN_PROGRESS;
import static pl.pacinho.muonlinewebtrader.model.enums.TradeOfferStatus.WAITING;

@RequiredArgsConstructor
@Controller
public class TradeController {

    private final WarehouseDecoder warehouseDecoder;
    private final TradeTools tradeTools;
    private final WebWarehouseItemService webWarehouseItemService;
    private final WebWalletService webWalletService;
    private final NotificationService notificationService;

    @GetMapping(UIConfig.TRADE_HOME)
    public String tradeHome(Model model,
                            Authentication authentication,
                            HttpSession session) {
        List<WareCellDto> tradeItems = (List<WareCellDto>) session.getAttribute("tradeItems");
        if (tradeItems == null) {
            tradeItems = ItemUtils.crateEmptyTrade();
            session.setAttribute("tradeItems", tradeItems);
        }
        Object success = session.getAttribute("success");
        if (success != null && (boolean) success) {
            session.removeAttribute("success");
        }

        model.addAttribute("success", success != null && (boolean) success);
        model.addAttribute("wareItems", tradeTools.filterItems(tradeItems, warehouseDecoder.decodeWebItems(webWarehouseItemService.getWarehouseItemsByAccountName(authentication.getName()))));
        model.addAttribute("tradeItems", ListUtils.partition(tradeItems, CodeUtils.TRADE_COL_COUNT));
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
        model.addAttribute("img", new TradeImages());
        model.addAttribute("colCount", CodeUtils.TRADE_COL_COUNT);
        return "trade";
    }

    @PostMapping(UIConfig.TRADE_PUT_ITEM)
    public String putItemToTrade(Model model,
                                 HttpSession session,
                                 Authentication authentication,
                                 @RequestParam("itemCode") String itemCode) {
        try {
            List<WareCellDto> tradeItems = tradeTools.putItem(authentication.getName(), (List<WareCellDto>) session.getAttribute("tradeItems"), itemCode);
            session.setAttribute("tradeItems", tradeItems);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return tradeHome(model, authentication, session);
        }
        return "redirect:" + UIConfig.TRADE_HOME;
    }

    @PostMapping(UIConfig.TRADE_SEND_OFFER)
    public String putItemToTrade(Model model,
                                 HttpSession session,
                                 String targetAccount,
                                 Authentication authentication) {
        try {
            if (tradeTools.sendOffer(session, authentication.getName(), targetAccount)) {
                session.setAttribute("tradeItems", ItemUtils.crateEmptyTrade());
                session.setAttribute("success", true);
            }
        } catch (Exception ex) {
            model.addAttribute("success", false);
            model.addAttribute("error", ex.getMessage());
            return tradeHome(model, authentication, session);
        }
        return "redirect:" + UIConfig.TRADE_HOME;
    }

    @PostMapping(UIConfig.TRADE_CLEAR_OFFER)
    public String clearOffer(HttpSession session) {
        session.setAttribute("tradeItems", ItemUtils.crateEmptyTrade());
        return "redirect:" + UIConfig.TRADE_HOME;
    }

    @PostMapping(UIConfig.TRADE_REMOVE_ITEM)
    public String removeItem(@PathVariable("id") String code, HttpSession session) {
        session.setAttribute("tradeItems", tradeTools.removeItem(code, session.getAttribute("tradeItems")));
        return "redirect:" + UIConfig.TRADE_HOME;
    }

    @GetMapping(UIConfig.TRADE_OFFERS_URl)
    public String tradeOffers(Model model, Authentication authentication) {
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
        model.addAttribute("offers", tradeTools.myOffers(authentication.getName()));
        return "trade-offers";
    }

    @GetMapping(UIConfig.TRADE_OFFER_DETAILS_URL)
    public String offerDetails(Model model,
                               Authentication authentication,
                               HttpSession session,
                               @PathVariable("offerId") String offerId) {
        try {
            TradeDto tradeDto = tradeTools.offerDetails(authentication.getName(), offerId);
            model.addAttribute("img", new TradeImages());
            model.addAttribute("offerDetails", tradeDto);
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
            model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
            List<WareCellDto> tradeItems = ItemUtils.crateEmptyTrade();
            if (tradeDto.getStatus() == IN_PROGRESS && tradeDto.getReceiver().getAccountName().equals(authentication.getName())) {
                List<WareCellDto> tempTradeItems = (List<WareCellDto>) session.getAttribute("tradeOfferItems" + offerId);
                if (tempTradeItems != null) {
                    tradeItems = tempTradeItems;
                }
                session.setAttribute("tradeOfferItems" + offerId, tradeItems);
                model.addAttribute("wareItems", tradeTools.filterItems(tradeItems, warehouseDecoder.decodeWebItems(webWarehouseItemService.getWarehouseItemsByAccountName(authentication.getName()))));
            }else if(tradeDto.getStatus() != IN_PROGRESS){
                tradeItems = tradeDto.getReceiver().getCells()
                        .stream()
                        .flatMap(List::stream)
                        .toList();
            }

            model.addAttribute("canAccept",
                    (tradeDto.getSender().getAccountName().equals(authentication.getName()) && tradeDto.getStatus() == WAITING)
                    || (tradeDto.getReceiver().getAccountName().equals(authentication.getName()) && tradeDto.getStatus() == IN_PROGRESS));
            model.addAttribute("inProgress", tradeDto.getSender().getAccountName().equals(authentication.getName()) && tradeDto.getStatus() == IN_PROGRESS);
            model.addAttribute("tradeItems", ListUtils.partition(tradeItems, CodeUtils.TRADE_COL_COUNT));

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return tradeOffers(model, authentication);
        }
        return "offer-details";
    }

    @PostMapping(UIConfig.TRADE_OFFER_PUT_ITEM)
    public String putItemToTradeOffer(Model model,
                                      HttpSession session,
                                      Authentication authentication,
                                      @PathVariable("offerId") String offerId,
                                      @RequestParam("itemCode") String itemCode) {
        try {
            List<WareCellDto> tradeItems = tradeTools.putItem(authentication.getName(), (List<WareCellDto>) session.getAttribute("tradeOfferItems" + offerId), itemCode);
            session.setAttribute("tradeOfferItems" + offerId, tradeItems);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return offerDetails(model, authentication, session, offerId);
        }
        model.addAttribute("offerId", offerId);
        return "redirect:" + UIConfig.TRADE_OFFER_DETAILS_URL;
    }

    @PostMapping(UIConfig.TRADE_OFFER_REMOVE_ITEM)
    public String removeOfferItem(@PathVariable("offerId") String offerId,
                                  @PathVariable("id") String code,
                                  HttpSession session,
                                  Model model) {
        session.setAttribute("tradeOfferItems" + offerId, tradeTools.removeItem(code, session.getAttribute("tradeOfferItems" + offerId)));
        model.addAttribute("offerId", offerId);
        return "redirect:" + UIConfig.TRADE_OFFER_DETAILS_URL;
    }

    @PostMapping(UIConfig.TRADE_OFFER_DECLINE)
    public String declineTradeOffer(@PathVariable("offerId") String offerId,
                                    HttpSession session,
                                    Authentication authentication,
                                    Model model) {
        try {
            tradeTools.declineTradeOffer(authentication.getName(), offerId);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return offerDetails(model, authentication, session, offerId);
        }
        session.removeAttribute("tradeOfferItems" + offerId);
        return "redirect:" + UIConfig.TRADE_OFFERS_URl;
    }

    @PostMapping(UIConfig.TRADE_OFFER_ACCEPT)
    public String acceptTradeOffer(@PathVariable("offerId") String offerId,
                                   HttpSession session,
                                   Authentication authentication,
                                   Model model) {
        try {
            tradeTools.acceptTradeOffer(authentication.getName(), offerId, (List<WareCellDto>) session.getAttribute("tradeOfferItems" + offerId));
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return offerDetails(model, authentication, session, offerId);
        }
        session.removeAttribute("tradeOfferItems" + offerId);
        return "redirect:" + UIConfig.TRADE_OFFERS_URl;
    }


}