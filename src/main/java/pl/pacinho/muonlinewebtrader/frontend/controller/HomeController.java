package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemDtoMapper;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final ItemShopService itemShopService;

    @GetMapping
    public String home2() {
        return "redirect:" + UIConfig.HOME_URL;
    }

    @GetMapping(UIConfig.HOME_URL)
    public String home(Model model) {
        model.addAttribute("items", ListUtils.partition( itemShopService.findMostViewedItems(12), 3));
        model.addAttribute("itemsRecentlyAdded", ListUtils.partition(itemShopService.findLastAdded(6),3));
        return "home";
    }
}