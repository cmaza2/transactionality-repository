package com.maza.accountsmovementsservice.domain.repository;

import com.maza.accountsmovementsservice.infraestructure.entities.AccountEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<AccountEntity,Long> {
    Mono<AccountEntity> findAccountByAccountNumber(String account);
    Flux<AccountEntity> findAccountEntityByIdCustomer(Long identification);
    Mono<AccountEntity> findAccountEntityByIdCustomerAndAccountNumber(Long identification, String account);

}
