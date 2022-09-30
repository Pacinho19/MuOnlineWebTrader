package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import pl.pacinho.muonlinewebtrader.entity.Transaction;
import pl.pacinho.muonlinewebtrader.model.dto.TransactionDto;

public class TransactionDtoMapper {

    public static TransactionDto parse(Transaction transaction) {
        return new TransactionDto(
                transaction.getDirection(),
                transaction.getType(),
                transaction.getMeansOfPayment(),
                transaction.getAmount(),
                transaction.getAddDate(),
                transaction.getDescription()
        );
    }

    public static Transaction parse(TransactionDto transactionDto) {
        return Transaction.builder()
                .amount(transactionDto.amount())
                .addDate(transactionDto.date())
                .description(transactionDto.description())
                .direction(transactionDto.direction())
                .meansOfPayment(transactionDto.meansOfPayment())
                .type(transactionDto.type())
                .build();
    }
}
