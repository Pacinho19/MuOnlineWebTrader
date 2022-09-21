package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemDtoMapper;
import pl.pacinho.muonlinewebtrader.service.ItemService;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping
    public String home2() {
        return "redirect:" + UIConfig.HOME_URL;
    }

    @GetMapping(UIConfig.HOME_URL)
    public String home(Model model) {
        model.addAttribute("items", ListUtils.partition(ItemDtoMapper.parseList(itemService.findRandomItems(12)), 3));
        model.addAttribute("itemsRecentlyAdded", ItemDtoMapper.parseList(itemService.findRandomItems(20)));
        return "home";
    }
}