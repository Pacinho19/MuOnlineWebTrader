package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.repository.WarehouseRepository;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

@RequiredArgsConstructor
@Controller
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseDecoder warehouseDecoder;

    @GetMapping(UIConfig.WAREHOUSE_URL)
    public String gameWarehouse(Model model, Authentication authentication) {
        model.addAttribute(
                "items",
                warehouseDecoder.decode(warehouseRepository.findByAccountName(authentication.getName()).getContent()));
        return "game-ware";
    }

    @GetMapping(UIConfig.WEB_WAREHOUSE_URL)
    public String webWarehouse(Model model) {
        return "web-ware";
    }
}
