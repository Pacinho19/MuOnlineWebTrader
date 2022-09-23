package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.repository.WarehouseRepository;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;
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
    private final AccountService accountService;

    @GetMapping(UIConfig.WAREHOUSE_URL)
    public String gameWarehouse(Model model, Authentication authentication) {
        model.addAttribute(
                "items",
                warehouseDecoder.decode(warehouseService.getWarehouseByAccountName(authentication.getName()).getContent()));
        return "game-ware";
    }

    @GetMapping(UIConfig.WEB_WAREHOUSE_URL)
    public String webWarehouse(Model model, Authentication authentication) {
        model.addAttribute(
                "items",
                warehouseDecoder.decodeWebItems(webWarehouseService.getWarehouseByAccountName(authentication.getName())));
        return "web-ware";
    }

    @Transactional
    @PostMapping(UIConfig.TRANSFER_TO_WEB_WAREHOUSE_URL)
    public String transferToWebWarehouse(@RequestParam("code") String code, Authentication authentication) {
        warehouseService.removeItem(code, authentication.getName());
        webWarehouseService.addItem(accountService.findByLogin(authentication.getName()), code);
        return "redirect:" + UIConfig.WEB_WAREHOUSE_URL;
    }

    @Transactional
    @PostMapping(UIConfig.TRANSFER_TO_GAME_WAREHOUSE_URL)
    public ModelAndView transferToGameWarehouse(@RequestParam("code") String code, Authentication authentication, ModelAndView modelAndView) {
        if (!warehouseTools.checkSpaceForPutItem(code)) {
            modelAndView.setViewName("web-ware");
            modelAndView.addObject("error", "Not enough space in game warehouse for transfer selected item!");
            return modelAndView;
        }
        try {
            webWarehouseService.removeItem(authentication.getName(), code);
            warehouseService.addItem(authentication.getName(), code);
        } catch (Exception ex) {
            modelAndView.setViewName("web-ware");
            modelAndView.addObject("error", ex.getMessage());
            return modelAndView;
        }
        return new ModelAndView("redirect:" + UIConfig.WAREHOUSE_URL);
    }

}