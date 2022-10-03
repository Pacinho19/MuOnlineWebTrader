package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.TradeImages;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.service.*;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.ItemUtils;
import pl.pacinho.muonlinewebtrader.tools.TradeTools;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TradeController {

    private final WarehouseDecoder warehouseDecoder;
    private final TradeTools tradeTools;
    private final TradeService tradeService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final WebWalletService webWalletService;
    private final NotificationService notificationService;

    @GetMapping(UIConfig.TRADE_HOME_URL)
    public String tradeHome(Model model,
                            Authentication authentication,
                            HttpSession session) {
        List<WareCellDto> tradeItems = (List<WareCellDto>) session.getAttribute("tradeItems");
        if (tradeItems == null) {
            tradeItems = ItemUtils.crateEmptyTrade();
            session.setAttribute("tradeItems", tradeItems);
        }
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
        return "redirect:" + UIConfig.TRADE_HOME_URL;
    }

    @PostMapping(UIConfig.TRADE_SEND_OFFER)
    public String putItemToTrade(Model model,
                                 HttpSession session,
                                 Authentication authentication) {
        try {
            tradeTools.sendOffer(session, authentication.getName());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return tradeHome(model, authentication, session);
        }
        return "redirect:" + UIConfig.TRADE_HOME_URL;
    }

    @PostMapping(UIConfig.TRADE_CLEAR_OFFER)
    public String clearOffer(HttpSession session) {
        session.setAttribute("tradeItems", ItemUtils.crateEmptyTrade());
        return "redirect:" + UIConfig.TRADE_HOME_URL;
    }

    @PostMapping(UIConfig.TRADE_REMOVE_ITEM)
    public String removeItem(@PathVariable("id") String code, HttpSession session) {
        session.setAttribute("tradeItems", tradeTools.removeItem(code, session.getAttribute("tradeItems")));
        return "redirect:" + UIConfig.TRADE_HOME_URL;
    }

}