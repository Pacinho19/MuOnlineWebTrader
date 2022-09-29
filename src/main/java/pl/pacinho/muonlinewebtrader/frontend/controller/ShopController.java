package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.filters.FilterDto;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;

import javax.servlet.http.HttpSession;
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
                           @RequestParam(value = "filters", required = false, defaultValue = "false") boolean filters,
                           HttpSession session) {

        FilterDto filterDto;
        if (!filters)
            filterDto = new FilterDto();
        else {
            filterDto = (FilterDto) session.getAttribute("filter");
            if (filterDto == null)
                filterDto = new FilterDto();
        }

        model.addAttribute("filter", filterDto);
        model.addAttribute("pageItems", itemShopService.findActiveOffers(page, filterDto));
        if (authentication != null)
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        return "shop";
    }

    @PostMapping(UIConfig.SHOP_URL)
    public String shopPage(@ModelAttribute FilterDto filterDto,
                           HttpSession session) {
        session.setAttribute("filter", filterDto);
        return "redirect:" + UIConfig.SHOP_URL + "?filters=true";
    }

    @GetMapping(UIConfig.SHOP_CLEAR_FILTERS)
    public String clearShopFilters(HttpSession session) {
        session.setAttribute("filter", new FilterDto());
        return "redirect:" + UIConfig.SHOP_URL;
    }
}