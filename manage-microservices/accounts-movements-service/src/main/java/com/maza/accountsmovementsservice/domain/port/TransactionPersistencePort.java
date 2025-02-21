package com.maza.accountsmovementsservice.domain.port;

import com.maza.accountsmovementsservice.domain.entities.Transaction;
import com.maza.accountsmovementsservice.domain.entities.Transactions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TransactionPersistencePort {
    Mono<Transaction> save(Transaction transaction);
    Mono<Transaction> findById(Long id);
    Mono<Void> deleteById(Long id);
    Flux<Transaction> findAll();
    Mono<Transaction> update(Transaction transaction);
    Mono<Transaction> findFirstByAccountNumberOrderByidDesc(Long idAccount);
    Flux<Transactions> getMovementsByAccounts(LocalDate initDate, LocalDate finalDate, Long accounts);
}
