package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ItemShopService itemShopService;

    @GetMapping(UIConfig.SHOP_URL)
    public String shopPage(Model model){
        model.addAttribute("items", itemShopService.findActiveOffers());
        return "shop";
    }
}
