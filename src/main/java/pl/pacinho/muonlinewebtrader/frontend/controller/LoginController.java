package pl.pacinho.muonlinewebtrader.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@Controller
public class LoginController {

    @GetMapping(UIConfig.LOGIN_URL)
    public String login(Model model) {
        model.addAttribute("backgroundImage", ImageUtils.getBackgroundImage());
        return "login";
    }

}
