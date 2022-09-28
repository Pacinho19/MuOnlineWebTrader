package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TransferJewelsDto {

    private List<String> itemsToRemove;
    private List<String> itemsToAdd;

    public TransferJewelsDto() {
        itemsToRemove = new ArrayList<>();
        itemsToAdd = new ArrayList<>();
    }

    public void putItemToRemove(String item) {
        itemsToRemove.add(item);
    }

    public void putItemToAdd(String item) {
        itemsToAdd.add(item);
    }
}