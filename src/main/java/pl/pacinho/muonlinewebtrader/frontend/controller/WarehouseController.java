package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;
import pl.pacinho.muonlinewebtrader.tools.WarehouseTools;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WebWarehouseService webWarehouseService;
    private final WarehouseDecoder warehouseDecoder;
    private final WarehouseTools warehouseTools;

    @GetMapping(UIConfig.WAREHOUSE_URL)
    public String gameWarehouse(Model model, Authentication authentication) {
        String warehouseContent = warehouseService.getWarehouseByAccountName(authentication.getName()).getContent();
        model.addAttribute(
                "items",
                warehouseDecoder.decode(warehouseContent));
        model.addAttribute(
                "warehouse",
                ListUtils.partition(
                        warehouseDecoder.decodeExtended(warehouseContent)
                        , CodeUtils.WAREHOUSE_ROW_SIZE));
        return "game-ware";
    }

    @GetMapping(UIConfig.WEB_WAREHOUSE_URL)
    public String webWarehouse(Model model, Authentication authentication) {
        model.addAttribute(
                "items",
                warehouseDecoder.decodeWebItems(webWarehouseService.getWarehouseByAccountName(authentication.getName())));
        return "web-ware";
    }

    @PostMapping(UIConfig.TRANSFER_TO_WEB_WAREHOUSE_URL)
    public String transferToWebWarehouse(Model model, @RequestParam("code") String code, Authentication authentication) {
        try {
            warehouseTools.transferToWeb(authentication.getName(), code);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return gameWarehouse(model, authentication);
        }
        return "redirect:" + UIConfig.WEB_WAREHOUSE_URL;
    }

    @PostMapping(UIConfig.TRANSFER_TO_GAME_WAREHOUSE_URL)
    public String transferToGameWarehouse(Model model, @RequestParam("code") String code, Authentication authentication) {
        try {
            warehouseTools.transferToGame(authentication.getName(), code);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return webWarehouse(model, authentication);
        }
        return "redirect:" + UIConfig.WAREHOUSE_URL;
    }

}