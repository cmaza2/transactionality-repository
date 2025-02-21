package com.maza.accountsmovementsservice.infraestructure.adapter.controller;

import com.maza.accountsmovementsservice.aplication.services.TransactionServices;
import com.maza.accountsmovementsservice.aplication.usecases.TransactionUseCase;
import com.maza.accountsmovementsservice.domain.dto.TransactionDTO;
import com.maza.accountsmovementsservice.domain.dto.request.TransactionRequestDTO;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import com.maza.accountsmovementsservice.domain.dto.TransactionsDTO;
import com.maza.accountsmovementsservice.infraestructure.adapter.out.GetCustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/movimientos")
@Tag(name = "MovementController", description = "Operations related to transactions carried out by accounts")
@Slf4j
public class TransactionController {

    private TransactionUseCase transactionUseCase;
    private GetCustomerService customerService;
    private KafkaTemplate kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    public TransactionController(TransactionServices transactionUseCase,
                                 GetCustomerService customerService,
                                 KafkaTemplate kafkaTemplate) {
        this.transactionUseCase = transactionUseCase;
        this.customerService = customerService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @GetMapping
    @ApiOperation(value = "getTransactions", notes = "gets all transactions made")
    public Flux<TransactionDTO> getTransactions() {
        return transactionUseCase.getTransactions()
                .doOnNext(transactions -> log.info("Trama de salida: {}", transactions));
    }

    @PostMapping
    @ApiOperation(value = "createTransaction", notes = "Register a new transaction")
    public Mono<TransactionDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) throws Exception {
        log.info("Trama de entrada: {}", transactionRequestDTO);
        return transactionUseCase.createTransaction(transactionRequestDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "updatetransaction", notes = "Update Transaction by id")
    public Mono<TransactionDTO> updateTransaction(@Valid @PathVariable Long id, @RequestBody TransactionRequestDTO transactionRequestDTO) {
        log.info("Id y trama de movimiento a actualizar: id={}, trama={}", id, transactionRequestDTO);
        return transactionUseCase.updateTransaction(id, transactionRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteTransaction", notes = "Delete transaction by id")
    public Mono<Void> deleteTransaction(@PathVariable Long id) {
        log.info("Id de movimiento a eliminar: id={}", id);
        return transactionUseCase.deleteTransaction(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "getTransactionById", notes = "Get transaction by id")
    public Mono<TransactionDTO> getTransactionById(@PathVariable Long id) {
        log.info("Id de movimiento a eliminar: id={}", id);
        return transactionUseCase.getTransactionById(id);
    }

    @GetMapping("/reportes")
    @ApiOperation(value = "getTransactionByUserAndDate", notes = "Gets all transactions made by date range and customer")
    public Flux<TransactionsDTO> getTransactionByUserAndDate(@RequestParam String fechaInicial,
                                                             @RequestParam String fechaFinal,
                                                             @RequestParam String cliente,
                                                             @RequestParam String numeroCuenta,
                                                             @RequestHeader("Authorization") String token) {
        log.info("Datos de entrada para reporteria de movimientos:{],{},{}", fechaInicial, fechaFinal, cliente);
        CustomerDTO customer = customerService.getCustomer(cliente, token);
        return transactionUseCase.getMovementsByUserAndDate(LocalDate.parse(fechaInicial), LocalDate.parse(fechaFinal), customer, numeroCuenta);
    }

    @GetMapping("/enviar_reporte")
    @ApiOperation(value = "sendAccountStatus", notes = "Gets all transactions made by date range and customer")
    public Flux<TransactionsDTO> sendAccountStatus(@RequestHeader("Authorization") String token,
                                                   @RequestParam String fechaInicial,
                                                   @RequestParam String fechaFinal,
                                                   @RequestParam String cliente,
                                                   @RequestParam String numeroCuenta) {
        log.info("Datos de entrada para enviar reporteria de movimientos:{],{},{}", fechaInicial, fechaFinal, cliente);
        CustomerDTO customer = customerService.getCustomer(cliente, token);
        Flux<TransactionsDTO> transactions = transactionUseCase.getMovementsByUserAndDate(LocalDate.parse(fechaInicial), LocalDate.parse(fechaFinal), customer, numeroCuenta);
        transactions.doOnNext(transaction -> kafkaTemplate.send(topicName, transaction));
        return transactions;
    }

}
