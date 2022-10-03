package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.muonlinewebtrader.service.NotificationService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;

@RequiredArgsConstructor
@Controller
public class MyErrorController implements ErrorController {

    private final WebWalletService webWalletService;
    private final NotificationService notificationService;

    @RequestMapping("/error")
    public String handleError(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("webWallet", webWalletService.findByAccountName(authentication.getName()));
            model.addAttribute("notifications", notificationService.findUnreadByAccount(authentication.getName()));
        }
        return "error";
    }
}