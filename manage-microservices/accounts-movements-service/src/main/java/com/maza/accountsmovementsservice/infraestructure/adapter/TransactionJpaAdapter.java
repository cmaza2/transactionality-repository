package com.maza.accountsmovementsservice.infraestructure.adapter;

import com.maza.accountsmovementsservice.domain.entities.Transaction;
import com.maza.accountsmovementsservice.domain.entities.Transactions;
import com.maza.accountsmovementsservice.domain.port.TransactionPersistencePort;
import com.maza.accountsmovementsservice.domain.repository.TransactionRepository;
import com.maza.accountsmovementsservice.infraestructure.mapper.TransactionDboMapper;
import com.maza.accountsmovementsservice.infraestructure.util.TransactionException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Transactional
public class TransactionJpaAdapter implements TransactionPersistencePort {
    private final TransactionRepository transactionRepository;
    private final TransactionDboMapper transactionDboMapper;
    private static final String MESSAGE_TRANSACTION = "TRANSACTION NOT FOUND";

    @Autowired
    public TransactionJpaAdapter(TransactionRepository transactionRepository, TransactionDboMapper transactionDboMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionDboMapper = transactionDboMapper;
    }

    @Override
    public Mono<Transaction> save(Transaction transaction) {
        var transactionToSave = transactionDboMapper.toDbo(transaction);
        return transactionRepository.save(transactionToSave)
                .map(transactionDboMapper::toDomain);
    }

    @Override
    public Mono<Transaction> update(Transaction transaction) {
        var transactionToSave = transactionDboMapper.toDbo(transaction);
        return transactionRepository.save(transactionToSave)
                .map(transactionDboMapper::toDomain);
    }

    @Override
    public Mono<Transaction> findById(Long id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new TransactionException(HttpStatus.NOT_FOUND,
                        String.format(MESSAGE_TRANSACTION, id))))
                .map(transactionDboMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        transactionRepository.deleteById(id);
        return Mono.empty();
    }

    @Override
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll()
                .map(transactionDboMapper::toDomain);
    }

    @Override
    public Mono<Transaction> findFirstByAccountNumberOrderByidDesc(Long idAccount) {
        return transactionRepository.findTopByIdAccountOrderByIdTransactionDesc(idAccount)
                .map(transactionDboMapper::toDomain);
    }

    @Override
    public Flux<Transactions> getMovementsByAccounts(LocalDate initDate, LocalDate finalDate, Long accounts) {
        return transactionRepository.findTransactionEntitiesByDateBetweenAndIdAccountOrderByIdTransactionDesc(initDate, finalDate, accounts)
                .map(transactionDboMapper::toDomainTransactions);
    }
}
