package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Transaction;
import pl.pacinho.muonlinewebtrader.model.dto.TransactionDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.TransactionDtoMapper;
import pl.pacinho.muonlinewebtrader.repository.TransactionRepository;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public List<TransactionDto> findAllByAccountName(String name) {
        return transactionRepository.findAllByAccountName(name)
                .stream()
                .map(TransactionDtoMapper::parse)
                .sorted(Comparator.comparing(TransactionDto::date).reversed())
                .toList();
    }

    public void addTransaction(String name, TransactionDto transactionDto) {
        Transaction transaction = TransactionDtoMapper.parse(transactionDto);
        transaction.setAccount(accountService.findByLogin(name));
        transactionRepository.save(transaction);
    }
}
