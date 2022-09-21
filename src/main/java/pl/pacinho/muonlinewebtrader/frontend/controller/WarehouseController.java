package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class WarehouseController {

    @GetMapping(UIConfig.WAREHOUSE_URL)
    public String gameWarehouse(Model model){
        return "game-ware";
    }

    @GetMapping(UIConfig.WEB_WAREHOUSE_URL)
    public String webWarehouse(Model model){
        return "web-ware";
    }
}
