package com.maza.accountsmovementsservice.aplication.services;

import com.maza.accountsmovementsservice.aplication.mapper.AccountRequestMapper;
import com.maza.accountsmovementsservice.aplication.usecases.AccountsUseCase;
import com.maza.accountsmovementsservice.domain.dto.AccountDTO;
import com.maza.accountsmovementsservice.domain.dto.request.AccountRequestDTO;
import com.maza.accountsmovementsservice.domain.port.AccountPersistencePort;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountServices implements AccountsUseCase {
    private final AccountPersistencePort accountPersistencePort;
    private final AccountRequestMapper accountRequestMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountServices(final AccountPersistencePort accountPersistencePort,
                           final AccountRequestMapper accountRequestMapper,
                           final ModelMapper modelMapper) {
        this.accountPersistencePort = accountPersistencePort;
        this.accountRequestMapper = accountRequestMapper;
        this.modelMapper = modelMapper;

    }

    /**
     * Method that calls the repository and list all accounts
     *
     * @return List of accounts.
     */
    @Override
    public Flux<AccountDTO> getAccounts() {
        return accountPersistencePort.findAll()
                .map(account -> modelMapper.map(account, AccountDTO.class));
    }
    /**
     * Method that calls the repository and recovery an account by identification
     *
     * @param idCustomer Id Number.
     * @param accountNumber Account.
     * @return Account information
     */
    @Override
    public Mono<AccountDTO> findByIdentificationAndAccount(Long idCustomer, String accountNumber) {
        log.info("Cuenta a buscar por identificacion: {}", idCustomer);
        return accountPersistencePort.findByIdentificationAndAcount(idCustomer, accountNumber)
                .map(account -> modelMapper.map(account, AccountDTO.class));
    }
    /**
     * Method that calls the repository and recovery an account by identification
     *
     * @param idCustomer Id Number.
     * @return Account information
     */
    @Override
    public Flux<AccountDTO> findByIdentification(Long idCustomer) {
        log.info("Cuenta a buscar por identificacion: {}", idCustomer);
        return accountPersistencePort.findByIdentification(idCustomer)
                .map(account -> modelMapper.map(account, AccountDTO.class));
    }

    /**
     * Method that calls the repository and create an account
     *
     * @param accountRequestDTO Account.
     * @return Account information
     */
    @Override
    public Mono<AccountDTO> create(AccountRequestDTO accountRequestDTO, CustomerDTO customerDTO) {
        var customer = accountRequestMapper.toDomain(accountRequestDTO);
        customer.setIdCustomer(customerDTO.getIdCustomer());
        return accountPersistencePort.save(customer)
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .doOnNext(customerResponseDTO -> log.info("Trama de salida crear cuenta: {}", customerResponseDTO));
    }

    @Override
    public Mono<AccountDTO> update(Long id, Long idCustomer, AccountRequestDTO accountRequestDTO) {
        var accountExists = accountRequestMapper.toDomain(accountRequestDTO);
        accountExists.setIdAccount(id);
        accountExists.setIdCustomer(idCustomer);
        return accountPersistencePort.update(accountExists)
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .doOnNext(accountResponseDto -> log.info("Trama de salida actualizar cuenta: {}", accountResponseDto));
    }

    /**
     * Method that calls the repository and return an account by id
     *
     * @param accountId id of account.
     * @return Account information
     */
    @Override
    public Mono<AccountDTO> getAccountById(Long accountId) {
        return accountPersistencePort.findById(accountId)
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .doOnNext(accountDTO -> log.info("Id y trama de salida obtener cuenta: id={},trama={}", accountId, accountDTO));
    }
    /**
     * Method that calls the repository and deletes an account
     *
     * @param accountId id of account.
     */
    @Override
    public Mono<Void> deleteAccount(Long accountId) {
        return accountPersistencePort.deleteById(accountId)
                .doOnSuccess(unused -> log.info("Cuenta con id {} eliminada correctamente", accountId));
    }

    /**
     * Method that calls the repository and return an account
     *
     * @param accountNumber number of account.
     * @return Account information
     */
    @Override
    public Mono<AccountDTO> getAccountInformation(String accountNumber) {
        return accountPersistencePort.getAccountInformation(accountNumber)
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .doOnNext(accountRequestDTO -> log.info("Información de la cuenta {} :{}", accountNumber, accountRequestDTO));
    }

}
