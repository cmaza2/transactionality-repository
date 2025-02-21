package com.maza.accountsmovementsservice.domain.repository;

import com.maza.accountsmovementsservice.infraestructure.entities.TransactionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
@Repository
public interface TransactionRepository extends R2dbcRepository<TransactionEntity,Long> {
    Mono<TransactionEntity> findTopByIdAccountOrderByIdTransactionDesc(Long idAccount);
    //@Query(value = "select m from ttransactions m where m.date between ?1 and ?2 and m.id_account = ?3 order by m.id_transaction desc")
    Flux<TransactionEntity> findTransactionEntitiesByDateBetweenAndIdAccountOrderByIdTransactionDesc(LocalDate initDatefechaInicial, LocalDate finalDate, Long accounts);
}
