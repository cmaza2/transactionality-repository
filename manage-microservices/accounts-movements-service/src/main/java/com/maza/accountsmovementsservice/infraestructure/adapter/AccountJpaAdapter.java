package com.maza.accountsmovementsservice.infraestructure.adapter;

import com.maza.accountsmovementsservice.domain.entities.Account;
import com.maza.accountsmovementsservice.domain.port.AccountPersistencePort;
import com.maza.accountsmovementsservice.domain.repository.AccountRepository;
import com.maza.accountsmovementsservice.infraestructure.mapper.AccountDboMapper;
import com.maza.accountsmovementsservice.infraestructure.util.AccountException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountJpaAdapter implements AccountPersistencePort {
    private final AccountRepository accountRepository;
    private final AccountDboMapper accountDboMapper;
    private ModelMapper modelMapper;
    private static final String MESSAGE_ACCOUNT = "ACCOUNT NOT FOUND";

    @Autowired
    public AccountJpaAdapter(AccountRepository accountRepository, AccountDboMapper accountDboMapper,ModelMapper modelMapper){
        this.accountRepository = accountRepository;
        this.accountDboMapper = accountDboMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<Account> save(Account accountDomain) {
        var userToSave = accountDboMapper.toDbo(accountDomain);
        return accountRepository.save(userToSave)
                .map(account -> modelMapper.map(account, Account.class));
    }

    @Override
    public Mono<Account> update(Account customerDomain) {
        var userToUpdate = accountDboMapper.toDbo(customerDomain);
        return accountRepository.save(userToUpdate)
                .map(account -> modelMapper.map(account, Account.class));
    }

    @Override
    public Flux<Account> findByIdentification(Long id) {
        return accountRepository.findAccountEntityByIdCustomer(id)
                .map(account -> modelMapper.map(account, Account.class))
                .switchIfEmpty(Mono.error(new AccountException(HttpStatus.NOT_FOUND, String.format(MESSAGE_ACCOUNT, id))));
    }

    @Override
    public Mono<Account> findByIdentificationAndAcount(Long id, String accountNumber) {
        return accountRepository.findAccountEntityByIdCustomerAndAccountNumber(id, accountNumber)
                .map(account -> modelMapper.map(account, Account.class));
    }

    @Override
    public Mono<Account> findById(Long id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new AccountException(HttpStatus.NOT_FOUND, String.format(MESSAGE_ACCOUNT, id))))
                .map(account -> modelMapper.map(account, Account.class));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return accountRepository.deleteById(id);
    }

    @Override
    public Flux<Account> findAll() {
        return accountRepository.findAll()
                .map(account -> modelMapper.map(account, Account.class));
    }

    @Override
    public Mono<Account> getAccountInformation(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new AccountException(HttpStatus.BAD_REQUEST, String.format(MESSAGE_ACCOUNT, accountNumber))))
                .map(account -> modelMapper.map(account, Account.class));
    }
}
