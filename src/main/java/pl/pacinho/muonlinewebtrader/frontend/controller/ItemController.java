package pl.pacinho.muonlinewebtrader.frontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.model.dto.PriceDto;
import pl.pacinho.muonlinewebtrader.model.dto.SimpleItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemDtoMapper;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;
import pl.pacinho.muonlinewebtrader.tools.ItemShopTools;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static pl.pacinho.muonlinewebtrader.frontend.config.UIConfig.ITEM_FOR_SALE;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemDecoder itemDecoder;
    private final ItemService itemService;
    private final ItemShopTools itemShopTools;

    @GetMapping(UIConfig.DECODE_ITEM_URL)
    public String decodeItemPage() {
        return "decode-item";
    }

    @PostMapping(UIConfig.DECODE_ITEM_URL)
    public ModelAndView decodeItem(@RequestParam("code") String code) {
        return new ModelAndView("decode-item", Map.of("itemDetails", itemDecoder.decode(code, -1), "code", code));
    }

    @GetMapping(UIConfig.ITEM_LIST_URL)
    public String itemList(Model model) {
        List<SimpleItemDto> items = ItemDtoMapper.parseList(itemService.findAll());
        model.addAttribute("items", items);
        return "item-list";
    }

    @PostMapping(UIConfig.PUT_FOR_SALE_ITEM_PAGE_URL)
    public String putForSalePage(Model model, PriceDto priceDto, @RequestParam("code") String code) {
        model.addAttribute("item", itemDecoder.decode(code, -1));
        model.addAttribute("prizeDto", priceDto != null ? priceDto : new PriceDto());
        return "put-for-sale";
    }

    @PostMapping(UIConfig.PUT_FOR_SALE_ITEM_URL)
    public String putForSale(Model model, String code, PriceDto priceDto, Authentication authentication) {
        try {
            itemShopTools.putForSale(code, priceDto, authentication.getName());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return putForSalePage(model, priceDto, code);
        }
        return "redirect:/" + UIConfig.ITEM_FOR_SALE + code;
    }

    @GetMapping(UIConfig.ITEM_FOR_SALE + "{code}")
    public String itemForSale(Model model, @PathVariable("code") String code) {
        try {
            Object obj = model.getAttribute("skipSearch");
            if (obj == null || !((boolean) obj))
                model.addAttribute("itemShop", itemShopTools.getByCode(code));

            model.addAttribute("skipSearch", false);
            if (model.getAttribute("itemShop") != null) itemShopTools.incrementItemViewCount(code);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("skipSearch", true);
            return itemForSale(model, code);
        }
        return "item-for-sale";
    }

    @PostMapping(UIConfig.BUY_ITEM_URL)
    public String buyItem(Model model, Authentication authentication, String code, PaymentMethod paymentMethod) {
        try {
            itemShopTools.buy(authentication.getName(), code, paymentMethod);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return itemForSale(model, code);
        }
        return "redirect:" + UIConfig.WEB_WAREHOUSE_URL;
    }


}