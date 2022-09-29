package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.*;
import pl.pacinho.muonlinewebtrader.tools.WarehouseTools;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final WebWalletService webWalletService;
    private final WarehouseTools warehouseTools;
    private final NotificationService notificationService;

    @GetMapping(UIConfig.ACCOUNT_URL)
    public String accountPage(Model model, Authentication authentication) {
        model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
        model.addAttribute("ware", warehouseTools.getPaymentsItem(authentication.getName()));
        model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
        return "account";
    }

    @PostMapping(UIConfig.BLESS_TRANSFER)
    public String blessTransfer(Model model, Authentication authentication, @RequestParam(value = "blessCount", required = false) Integer blessCount) {
        try {
            warehouseTools.transferBlessToWallet(authentication.getName(), blessCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

    @PostMapping(UIConfig.SOUL_TRANSFER)
    public String soulTransfer(Model model, Authentication authentication, @RequestParam(value = "soulCount", required = false) Integer soulCount) {
        try {
            warehouseTools.transferSoulToWallet(authentication.getName(), soulCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }
    @PostMapping(UIConfig.ZEN_TRANSFER)
    public String zenTransfer(Model model, Authentication authentication, @RequestParam(value = "zenCount", required = false) Integer soulCount) {
        try {
            warehouseTools.transferZenToWallet(authentication.getName(), soulCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

    @PostMapping(UIConfig.BLESS_DISBURSEMENT)
    public String blessDisbursement(Model model, Authentication authentication, @RequestParam(value = "blessCount", required = false) Integer blessCount) {
        try {
            warehouseTools.disbursementBlessFromWallet(authentication.getName(), blessCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

    @PostMapping(UIConfig.SOUL_DISBURSEMENT)
    public String soulDisbursement(Model model, Authentication authentication, @RequestParam(value = "soulCount", required = false) Integer soulCount) {
        try {
            warehouseTools.disbursementSoulFromWallet(authentication.getName(), soulCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

    @PostMapping(UIConfig.ZEN_DISBURSEMENT)
    public String zenDisbursement(Model model, Authentication authentication, @RequestParam(value = "zenCount", required = false) Integer zenCount) {
        try {
            warehouseTools.disbursementZenFromWallet(authentication.getName(), zenCount);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return accountPage(model, authentication);
        }
        return "redirect:" + UIConfig.ACCOUNT_URL;
    }

}