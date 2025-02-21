package com.maza.accountsmovementsservice.aplication.usecases;

import com.maza.accountsmovementsservice.domain.dto.TransactionDTO;
import com.maza.accountsmovementsservice.domain.dto.TransactionsDTO;
import com.maza.accountsmovementsservice.domain.dto.request.TransactionRequestDTO;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionUseCase {
    Mono<TransactionDTO> createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
    Mono<TransactionDTO> updateTransaction(Long id, TransactionRequestDTO transactionRequestDTO);
    Mono<TransactionDTO> getTransactionById(Long transactionId);
    Flux<TransactionDTO> getTransactions();
    Mono<Void> deleteTransaction(Long transactionId);
    Mono<BigDecimal> getBalance(String accountNumber);
    Flux<TransactionsDTO> getMovementsByUserAndDate(LocalDate initDate, LocalDate finalDate, CustomerDTO customer,String accountNumber);
}
