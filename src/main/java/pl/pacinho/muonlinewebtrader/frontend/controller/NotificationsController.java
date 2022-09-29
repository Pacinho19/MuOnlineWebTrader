package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationService notificationService;

    @PostMapping(UIConfig.READ_NOTIFICATION)
    public String notification(HttpServletRequest request,
                               Authentication authentication,
                               @PathVariable("id") long id) {
        notificationService.read(id, authentication.getName());
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping(UIConfig.READ_ALL_NOTIFICATIONS)
    public String readAllNotification(HttpServletRequest request,
                                      Authentication authentication) {
        notificationService.setAllAsRead(authentication.getName());
        return "redirect:" + request.getHeader("Referer");
    }
}