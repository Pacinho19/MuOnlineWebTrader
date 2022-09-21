package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemDtoMapper;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemDecoder itemDecoder;
    private final ItemService itemService;

    @GetMapping(UIConfig.DECODE_ITEM_URL)
    public String decodeItemPage() {
        return "decode-item";
    }

    @PostMapping(UIConfig.DECODE_ITEM_URL)
    public ModelAndView decodeItem(@RequestParam("code") String code) {
        return new ModelAndView("decode-item", Map.of("itemDetails", itemDecoder.decode(code), "code", code));
    }

    @GetMapping(UIConfig.ITEM_LIST_URL)
    public String itemList(Model model) {
        List<ItemDto> items = ItemDtoMapper.parseList(itemService.findAll());
        model.addAttribute("items", items);
        return "item-list";
    }


}
