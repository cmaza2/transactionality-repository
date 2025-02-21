package com.maza.accountsmovementsservice.infraestructure.adapter.unit;

import com.maza.accountsmovementsservice.domain.entities.Transaction;
import com.maza.accountsmovementsservice.domain.entities.Transactions;
import com.maza.accountsmovementsservice.domain.repository.TransactionRepository;
import com.maza.accountsmovementsservice.infraestructure.adapter.TransactionJpaAdapter;
import com.maza.accountsmovementsservice.infraestructure.entities.TransactionEntity;
import com.maza.accountsmovementsservice.infraestructure.mapper.TransactionDboMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionJpaAdapterTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionJpaAdapter transactionJpaAdapter;
    @Mock
    private TransactionDboMapper transactionDboMapper;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTransactionSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transaction transaction = getTransaction();
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(Mono.just(transactionEntity));
        when(transactionDboMapper.toDbo(transaction)).thenReturn(transactionEntity);
        when(transactionDboMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Mono<Transaction> result = transactionJpaAdapter.save(transaction);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.block().getTransactionType());
    }

    @Test
    void updateTransactionSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transaction transaction = getTransaction();
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(Mono.just(transactionEntity));
        when(transactionDboMapper.toDbo(transaction)).thenReturn(transactionEntity);
        when(transactionDboMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Mono<Transaction> result = transactionJpaAdapter.update(transaction);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.block().getTransactionType());
    }

    @Test
    void findTransactionByIdSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transaction transaction = getTransaction();
        when(transactionRepository.findById(1L)).thenReturn(Mono.just(transactionEntity));
        when(transactionDboMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Mono<Transaction> result = transactionJpaAdapter.findById(1L);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.block().getTransactionType());
    }

    @Test
    void deleteTransactionByIdSuccessfully() {
        when(transactionRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = transactionJpaAdapter.deleteById(1L);

        assertNotNull(result);
        assertEquals(Mono.empty(), result);
    }

    @Test
    void findAllTransactionsSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transaction transaction = getTransaction();
        when(transactionRepository.findAll()).thenReturn(Flux.just(transactionEntity));
        when(transactionDboMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Flux<Transaction> result = transactionJpaAdapter.findAll();

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.blockFirst().getTransactionType());
    }

    @Test
    void findFirstByAccountNumberOrderByIdDescSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transaction transaction = getTransaction();
        when(transactionRepository.findTopByIdAccountOrderByIdTransactionDesc(1L)).thenReturn(Mono.just(transactionEntity));
        when(transactionDboMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Mono<Transaction> result = transactionJpaAdapter.findFirstByAccountNumberOrderByidDesc(1L);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.block().getTransactionType());
    }

    @Test
    void getMovementsByAccountsSuccessfully() {
        TransactionEntity transactionEntity = getTransactionEntity();
        Transactions transactions = new Transactions();
        when(transactionRepository.findTransactionEntitiesByDateBetweenAndIdAccountOrderByIdTransactionDesc(any(LocalDate.class), any(LocalDate.class), anyLong())).thenReturn(Flux.just(transactionEntity));
        when(transactionDboMapper.toDomainTransactions(transactionEntity)).thenReturn(transactions);

        Flux<Transactions> result = transactionJpaAdapter.getMovementsByAccounts(LocalDate.now(), LocalDate.now(), 1L);

        assertNotNull(result);
        assertEquals(transactions, result.blockFirst());
    }

    private TransactionEntity getTransactionEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionType("Retiro");
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setIdAccount(Long.valueOf(1));
        transactionEntity.setValue(new BigDecimal(400));
        transactionEntity.setBalance(new BigDecimal(1600));
        return transactionEntity;
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType("Retiro");
        transaction.setDate(LocalDate.now());
        transaction.setIdAccount(Long.valueOf(1));
        transaction.setValue(new BigDecimal(400));
        transaction.setBalance(new BigDecimal(1600));
        return transaction;
    }
}