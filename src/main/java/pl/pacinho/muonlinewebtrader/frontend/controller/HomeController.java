package pl.pacinho.muonlinewebtrader.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping
    public String home2() {
        return "redirect:"+UIConfig.HOME_URL;
    }

    @GetMapping(UIConfig.HOME_URL)
    public String home() {
        return "home";
    }
}
