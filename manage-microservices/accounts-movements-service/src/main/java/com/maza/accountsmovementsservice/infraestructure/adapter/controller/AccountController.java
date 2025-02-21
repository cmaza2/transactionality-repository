package com.maza.accountsmovementsservice.infraestructure.adapter.controller;

import com.maza.accountsmovementsservice.aplication.usecases.AccountsUseCase;
import com.maza.accountsmovementsservice.domain.dto.AccountDTO;
import com.maza.accountsmovementsservice.domain.dto.request.AccountRequestDTO;
import com.maza.accountsmovementsservice.domain.entities.Account;
import com.maza.accountsmovementsservice.infraestructure.util.ResponseObject;
import com.maza.accountsmovementsservice.infraestructure.adapter.out.GetCustomerService;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cuentas")
@Slf4j
@Api(tags = "AccountController", description = "Operations related to accounts")
public class AccountController {
    private AccountsUseCase accountsUseCase;
    private GetCustomerService customerService;

    @Autowired
    public AccountController(AccountsUseCase accountsUseCase, GetCustomerService customerService) {
        this.accountsUseCase = accountsUseCase;
        this.customerService = customerService;
    }

    @GetMapping
    @ApiOperation(value = "getAccounts", notes = "List a register accounts")
    public Flux<AccountDTO> getAccounts() {
        Flux<AccountDTO> accounts = accountsUseCase.getAccounts();
        log.info("Trama de salida: {}", accounts);
        return accounts;
    }

    @PostMapping
    @ApiOperation(value = "createAccount", notes = "Register a new account")
    public Mono<AccountDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO, @RequestHeader("Authorization") String token) {
        log.info("Trama de entrada: {}", accountRequestDTO);
        CustomerDTO customerDTO = customerService.getCustomer(accountRequestDTO.getCustomer(), token);
        Mono<AccountDTO> createAccount = accountsUseCase.create(accountRequestDTO, customerDTO);
        return createAccount;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "updateAccount", notes = "Update account by Id")
    public Mono<AccountDTO> updateAccount(@Valid @PathVariable Long id, @RequestBody AccountRequestDTO accountRequestDTO, @RequestHeader("Authorization") String token) {
        log.info("Id y trama a actualizar  id={}, trama={}", id, accountRequestDTO);
        CustomerDTO customerDTO = customerService.getCustomer(accountRequestDTO.getCustomer(), token);
        return accountsUseCase.update(id, customerDTO.getIdCustomer(), accountRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteAccount", notes = "Delete account by Id")
    public Mono<Void> deleteAccount(@PathVariable Long id) {
        log.info("Id a eliminar: {}", id);
        accountsUseCase.deleteAccount(id);
        return  accountsUseCase.deleteAccount(id);
    }

    @GetMapping("/identificacion")
    @ApiOperation(value = "findAccountByIdentification", notes = "Find account by Identification Number")
    public Flux<AccountDTO> getAccountByIdentification(@Valid @PathParam("identification") String identification, @RequestHeader("Authorization") String token) {
        log.info("Identificacion a buscar {}", identification);
        CustomerDTO customerDTO = customerService.getCustomer(identification, token);
        return accountsUseCase.findByIdentification(customerDTO.getIdCustomer());

    }

    @GetMapping("/identificacion&Cuenta")
    @ApiOperation(value = "findAccountByIdentificationAndAccount", notes = "Find account by Identification Number")
    public Mono<AccountDTO> getAccountByIdentificationAndAccount(@Valid @PathParam("identification") String identification, @Valid @PathParam("account") String account, @RequestHeader("Authorization") String token) {
        log.info("Identificacion a buscar {}", identification);
        CustomerDTO customerDTO = customerService.getCustomer(identification, token);
        return accountsUseCase.findByIdentificationAndAccount(customerDTO.getIdCustomer(), account);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "getAccountById", notes = "Find account by Id")
    public Mono<AccountDTO> getAccountById(@Valid @PathVariable Long id) {
        log.info("Id a buscar  id={}", id);
        return accountsUseCase.getAccountById(id);
    }

    @GetMapping("/cuenta")
    @ApiOperation(value = "getAccountInformationByAccount", notes = "Find account by accountNumber")
    public Mono<AccountDTO> getAccountInformationByAccount(@Valid @PathParam("account") String account) {
        log.info("Cuenta a buscar  cuenta={}", account);
        return accountsUseCase.getAccountInformation(account);
    }
}
