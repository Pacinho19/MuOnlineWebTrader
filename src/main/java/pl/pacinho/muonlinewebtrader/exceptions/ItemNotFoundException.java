package pl.pacinho.muonlinewebtrader.exceptions;

public class ItemNotFoundException extends IllegalStateException {
    public ItemNotFoundException(String textContent) {
        super(textContent);
    }
}
