package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;
    private final WebWalletService webWalletService;

    @GetMapping(UIConfig.ACCOUNT_URL)
    public String accountPage(Model model, Authentication authentication) {
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        return "account";
    }
}