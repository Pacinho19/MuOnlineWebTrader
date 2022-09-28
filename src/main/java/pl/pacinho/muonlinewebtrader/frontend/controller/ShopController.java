package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
                           FilterDto filterDto,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           HttpSession session) {

        if (filterDto == null && page.isPresent()) filterDto = (FilterDto) session.getAttribute("filter");
        else if (page.isEmpty()) {
            filterDto = new FilterDto();
            session.setAttribute("filter", filterDto);
        } else session.setAttribute("filter", filterDto);

        model.addAttribute("filter", filterDto);
        model.addAttribute("pageItems", itemShopService.findActiveOffers(page, size, filterDto));
        if (authentication != null)
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        return "shop";
    }
}