package com.maza.accountsmovementsservice.domain.port;

import com.maza.accountsmovementsservice.domain.entities.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountPersistencePort {
    Mono<Account> save(Account account);
    Mono<Account> findById(Long id);
    Mono<Void> deleteById(Long id);
    Flux<Account> findAll();
    Mono<Account> update(Account account);
    Flux<Account> findByIdentification(Long id);
    Mono<Account> findByIdentificationAndAcount(Long id,String accountNumber);
    Mono<Account> getAccountInformation(String accountNumber);
}
