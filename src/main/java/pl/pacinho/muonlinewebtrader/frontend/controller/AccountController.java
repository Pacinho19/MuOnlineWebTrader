package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;
import pl.pacinho.muonlinewebtrader.tools.WarehouseTools;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;
    private final WebWalletService webWalletService;
    private final WarehouseTools warehouseTools;

    @GetMapping(UIConfig.ACCOUNT_URL)
    public String accountPage(Model model, Authentication authentication) {
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        model.addAttribute("ware", warehouseTools.getPaymentsItem(authentication.getName()));
        return "account";
    }

    @PostMapping(UIConfig.BLESS_TRANSFER)
    public String transferBless(Model model, Authentication authentication, @RequestParam(value = "blessCount", required = false) Integer blessCount) {
        try {
            warehouseTools.transferBlessToWallet(authentication.getName(), blessCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

    @PostMapping(UIConfig.SOUL_TRANSFER)
    public String transferSoul(Model model, Authentication authentication, @RequestParam(value = "soulCount", required = false) Integer soulCount) {
        try {
            warehouseTools.transferSoulToWallet(authentication.getName(), soulCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }
    @PostMapping(UIConfig.ZEN_TRANSFER)
    public String transferZen(Model model, Authentication authentication, @RequestParam(value = "zenCount", required = false) Integer soulCount) {
        try {
            warehouseTools.transferZenToWallet(authentication.getName(), soulCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

}