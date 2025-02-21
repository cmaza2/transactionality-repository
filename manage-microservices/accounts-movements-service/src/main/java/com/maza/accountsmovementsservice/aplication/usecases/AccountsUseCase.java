package com.maza.accountsmovementsservice.aplication.usecases;

import com.maza.accountsmovementsservice.domain.dto.AccountDTO;
import com.maza.accountsmovementsservice.domain.dto.request.AccountRequestDTO;
import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import com.maza.accountsmovementsservice.domain.entities.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountsUseCase {
    Mono<AccountDTO> create(AccountRequestDTO accountRequestDTO, CustomerDTO customerDTO);
    Mono<AccountDTO> update(Long id, Long idCustomer, AccountRequestDTO accountRequestDTO);
    Mono<AccountDTO> getAccountById(Long accountId);
    Mono<Void> deleteAccount(Long accountId);
    Flux<AccountDTO> getAccounts();
    Mono<AccountDTO> findByIdentificationAndAccount(Long id, String accountNumber);
    Flux<AccountDTO> findByIdentification(Long id);
    Mono<AccountDTO> getAccountInformation(String accountNumber);


}
