package pl.pacinho.muonlinewebtrader.model.dto;

import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDirection;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionType;

import java.time.LocalDateTime;

public record TransactionDto(TransactionDirection direction, TransactionType type, PaymentMethod meansOfPayment,
                             Long amount, LocalDateTime date, String description) {
}
