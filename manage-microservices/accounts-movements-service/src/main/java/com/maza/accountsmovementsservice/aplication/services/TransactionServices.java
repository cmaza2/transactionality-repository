package com.maza.accountsmovementsservice.aplication.services;

import com.maza.accountsmovementsservice.aplication.mapper.TransactionRequestMapper;
import com.maza.accountsmovementsservice.domain.dto.TransactionDTO;
import com.maza.accountsmovementsservice.domain.dto.TransactionsDTO;
import com.maza.accountsmovementsservice.domain.dto.request.TransactionRequestDTO;
import com.maza.accountsmovementsservice.domain.entities.Account;
import com.maza.accountsmovementsservice.domain.port.AccountPersistencePort;
import com.maza.accountsmovementsservice.domain.port.TransactionPersistencePort;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import com.maza.accountsmovementsservice.aplication.usecases.TransactionUseCase;
import com.maza.accountsmovementsservice.aplication.util.TypeMovement;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class TransactionServices implements TransactionUseCase {
    private final TransactionPersistencePort transactionPersistencePort;
    private final AccountPersistencePort accountPersistencePort;
    private final TransactionRequestMapper transactionRequestMapper;
    private final ModelMapper modelMapper;


    @Autowired
    public TransactionServices(AccountPersistencePort accountPersistencePort,
                               TransactionPersistencePort transactionPersistencePort,
                               TransactionRequestMapper transactionRequestMapper,
                               ModelMapper modelMapper) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.transactionRequestMapper = transactionRequestMapper;
        this.modelMapper = modelMapper;
        this.accountPersistencePort = accountPersistencePort;
    }

    @Override
    public Flux<TransactionDTO> getTransactions() {
        return transactionPersistencePort.findAll()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class));
    }

    @Override
    public Mono<TransactionDTO> getTransactionById(Long transactionId) {
        return transactionPersistencePort.findById(transactionId)
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .doOnNext(transactionRequestDTO -> log.info("Trama de respuesta buscar transaccion por id {}: {}", transactionId, transactionRequestDTO));
    }

    @Override
    public Mono<TransactionDTO> createTransaction(TransactionRequestDTO transactionRequestDTO) {
        String accountNumber = transactionRequestDTO.getAccount();
        return accountPersistencePort.getAccountInformation(accountNumber)
                .switchIfEmpty(Mono.error(new Exception("Account not found")))
                .flatMap(account -> {
                    Account validatedAccount = TypeMovement.validateExistAccount(account, accountNumber);
                    return getBalance(validatedAccount.getAccountNumber())
                            .defaultIfEmpty(validatedAccount.getInitialBalance())
                            .flatMap(balance -> {
                                BigDecimal updatedBalance = TypeMovement.movement(transactionRequestDTO.getTransactionType(),
                                        balance, transactionRequestDTO.getValue());
                                var transaction = transactionRequestMapper.toDomain(transactionRequestDTO);
                                transaction.setIdAccount(validatedAccount.getIdAccount());
                                transaction.setBalance(updatedBalance);
                                transaction.setDate(LocalDate.now());
                                transaction.setValue(transaction.getTransactionType().toLowerCase().equals("retiro") ? new BigDecimal("-" + transaction.getValue()) : transaction.getValue());
                                return transactionPersistencePort.save(transaction)
                                        .map(transactionDto -> modelMapper.map(transactionDto, TransactionDTO.class))
                                        .doOnNext(transactionResponseDTO -> log.info("Trama de salida crear movimiento: {}", transactionResponseDTO));
                            });
                });
    }

    @Override
    public Mono<TransactionDTO> updateTransaction(Long id, TransactionRequestDTO transactionRequestDTO) {
        return accountPersistencePort.getAccountInformation(transactionRequestDTO.getAccount())
                .switchIfEmpty(Mono.error(new Exception("Account not found")))
                .flatMap(account -> {
                    var transactionExists = transactionRequestMapper.toDomain(transactionRequestDTO);
                    transactionExists.setIdTransaction(id);
                    transactionExists.setIdAccount(account.getIdAccount());
                    return transactionPersistencePort.save(transactionExists)
                            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                            .doOnNext(transactionResponseDTO -> log.info("Trama de salida actualizar movimiento por id: id={}, trama= {}", id, transactionResponseDTO));
                });
    }

    @Override
    public Mono<Void> deleteTransaction(Long transactionId) {
        return transactionPersistencePort.deleteById(transactionId);
    }

    @Override
    public Mono<BigDecimal> getBalance(String accountNumber) {
        return accountPersistencePort.getAccountInformation(accountNumber)
                .switchIfEmpty(Mono.error(new Exception("Account not found")))
                .flatMap(account -> transactionPersistencePort.findFirstByAccountNumberOrderByidDesc(account.getIdAccount())
                        .map(transaction -> transaction.getBalance())
                        .defaultIfEmpty(account.getInitialBalance()));
    }


    @Override
    public Flux<TransactionsDTO> getMovementsByUserAndDate(LocalDate initDate, LocalDate finalDate, CustomerDTO customer, String accountNumber) {
        return accountPersistencePort.findByIdentificationAndAcount(customer.getIdCustomer(), accountNumber)
                .switchIfEmpty(Mono.error(new Exception("Account not found")))
                .flatMapMany(account -> transactionPersistencePort.getMovementsByAccounts(initDate, finalDate, account.getIdAccount())
                        .map(transaction -> {
                            TransactionsDTO transactionsDTO = new TransactionsDTO();
                            transactionsDTO.setDate(transaction.getDate());
                            transactionsDTO.setCustomer(customer.getName());
                            transactionsDTO.setAccountNumber(account.getAccountNumber());
                            transactionsDTO.setAccountType(account.getAccountType());
                            transactionsDTO.setInitialBalance(account.getInitialBalance());
                            transactionsDTO.setStatus(account.isStatus());
                            transactionsDTO.setTransactionType(transaction.getTransactionType());
                            transactionsDTO.setValue(transaction.getValue());
                            transactionsDTO.setBalance(transaction.getBalance());
                            return transactionsDTO;
                        })
                        .doOnNext(transactionsDTO -> log.info("Transacciones realizadas por cliente {}:", transactionsDTO)));
    }
}
