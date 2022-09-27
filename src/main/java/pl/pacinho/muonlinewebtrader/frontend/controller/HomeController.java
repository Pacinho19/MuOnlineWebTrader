package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final WebWalletService webWalletService;
    private final ItemShopService itemShopService;

    @GetMapping
    public String home2() {
        return "redirect:" + UIConfig.HOME_URL;
    }

    @GetMapping(UIConfig.HOME_URL)
    public String home(Model model, Authentication authentication) {
        model.addAttribute("items", ListUtils.partition(itemShopService.findMostViewedItems(12), 3));
        model.addAttribute("itemsRecentlyAdded", ListUtils.partition(itemShopService.findLastAdded(6), 3));

        if (authentication != null)
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        return "home";
    }
}