package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.CurrencyRateDto;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.service.NotificationService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;
import pl.pacinho.muonlinewebtrader.tools.ExchangeTools;

@RequiredArgsConstructor
@Controller
public class CantorController {

    private final WebWalletService webWalletService;
    private final NotificationService notificationService;
    private final ExchangeTools exchangeTools;

    @GetMapping(UIConfig.CANTOR_URL)
    public String cantorPage(Model model,
                             Authentication authentication) {
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
        model.addAttribute("currencyRate", new CurrencyRateDto());
        return "cantor";
    }

    @PostMapping(UIConfig.CANTOR_EXCHANGE_URL)
    public String exchange(@RequestParam("count") Long count,
                           @RequestParam("type") PaymentMethod type,
                           Model model,
                           Authentication authentication) {
        try {
            exchangeTools.exchange(authentication.getName(), count, type);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return cantorPage(model, authentication);
        }
        return "redirect:" + UIConfig.CANTOR_URL;
    }
}
