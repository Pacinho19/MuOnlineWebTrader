package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceDto {

    private long priceZen;
    private long priceBless;
    private long priceSoul;

    public PriceDto() {
        priceZen = 0L;
        priceBless = 0L;
        priceSoul = 0L;
    }
}
