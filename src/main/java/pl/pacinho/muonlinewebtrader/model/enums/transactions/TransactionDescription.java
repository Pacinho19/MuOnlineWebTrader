package pl.pacinho.muonlinewebtrader.model.enums.transactions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionDescription {

    SELF_PAYMENT("Self payment"),
    SELF_DISBURSEMENT("Self disbursement"),
    ITEM_PURCHASE("Item purchase"),
    ITEM_SALES("Item sales");

    @Getter
    private final String text;
}
