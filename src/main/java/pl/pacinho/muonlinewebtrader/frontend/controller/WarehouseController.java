package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.WarehouseDto;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;
import pl.pacinho.muonlinewebtrader.tools.WarehouseTools;

@RequiredArgsConstructor
@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final WebWarehouseService webWarehouseService;
    private final WarehouseDecoder warehouseDecoder;
    private final WarehouseTools warehouseTools;

    @GetMapping(UIConfig.WAREHOUSE_URL)
    public String gameWarehouse(Model model, Authentication authentication) {
        Warehouse ware = warehouseService.getWarehouseByAccountName(authentication.getName());
        model.addAttribute(
                "ware",
                new WarehouseDto(warehouseDecoder.decode(ware.getContent()), ware.getZen())
        );
        model.addAttribute(
                "warehouse",
                ListUtils.partition(
                        warehouseDecoder.decodeExtended(ware.getContent())
                        , CodeUtils.WAREHOUSE_ROW_SIZE));
        return "game-ware";
    }

    @GetMapping(UIConfig.WEB_WAREHOUSE_URL)
    public String webWarehouse(Model model, Authentication authentication) {
        model.addAttribute(
                "items",
                warehouseDecoder.decodeWebItems(webWarehouseItemService.getWarehouseItemsByAccountName(authentication.getName())));
        model.addAttribute("zen", webWarehouseService.getWarehouseByAccountName(authentication.getName()).getZen());
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

    @PostMapping(UIConfig.TRANSFER_ZEN_TO_WEB_WAREHOUSE_URL)
    public String transferZenToWebWarehouse(Model model, @RequestParam(value = "zen", required = false) Long zen, Authentication authentication) {
        try {
            warehouseTools.transferZenToWebWarehouse(authentication.getName(), zen);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return gameWarehouse(model, authentication);
        }
        return "redirect:" + UIConfig.WEB_WAREHOUSE_URL;
    }

    @PostMapping(UIConfig.TRANSFER_ZEN_TO_GAME_WAREHOUSE_URL)
    public String transferZenToGameWarehouse(Model model, @RequestParam(value = "zen", required = false) Long zen, Authentication authentication) {
        try {
            warehouseTools.transferZenToGameWarehouse(authentication.getName(), zen);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return webWarehouse(model, authentication);
        }
        return "redirect:" + UIConfig.WAREHOUSE_URL;
    }



}