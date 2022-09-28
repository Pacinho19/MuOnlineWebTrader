package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ItemShopService itemShopService;
    private final WebWalletService webWalletService;

    @GetMapping(UIConfig.SHOP_URL)
    public String shopPage(Model model,
                           Authentication authentication,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        model.addAttribute("pageItems", itemShopService.findActiveOffers(page, size));
        if (authentication != null)
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        return "shop";
    }
}