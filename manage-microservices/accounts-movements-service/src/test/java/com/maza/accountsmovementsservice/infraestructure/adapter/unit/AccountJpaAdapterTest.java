package com.maza.accountsmovementsservice.infraestructure.adapter.unit;



import static org.junit.jupiter.api.Assertions.*;

import com.maza.accountsmovementsservice.domain.entities.Account;
import com.maza.accountsmovementsservice.domain.repository.AccountRepository;
import com.maza.accountsmovementsservice.infraestructure.adapter.AccountJpaAdapter;
import com.maza.accountsmovementsservice.infraestructure.entities.AccountEntity;
import com.maza.accountsmovementsservice.infraestructure.mapper.AccountDboMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountJpaAdapterTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountJpaAdapter accountJpaAdapter;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AccountDboMapper accountDboMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void saveAccountSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(Mono.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);
        when(accountDboMapper.toDbo(account)).thenReturn(accountEntity);

        Mono<Account> result = accountJpaAdapter.save(account);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.block().getAccountNumber());
    }
    @Test
    void updateAccountSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(Mono.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);
        when(accountDboMapper.toDbo(account)).thenReturn(accountEntity);
        Mono<Account> result = accountJpaAdapter.update(account);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.block().getAccountNumber());
    }
    @Test
    void findAccountByIdentificationSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.findAccountEntityByIdCustomer(11L)).thenReturn(Flux.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);

        Flux<Account> result = accountJpaAdapter.findByIdentification(11L);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.blockFirst().getAccountNumber());
    }
    @Test
    void findAccountByIdentificationAndAccountSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.findAccountEntityByIdCustomerAndAccountNumber(11L, "59020304")).thenReturn(Mono.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);

        Mono<Account> result = accountJpaAdapter.findByIdentificationAndAcount(11L, "59020304");

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.block().getAccountNumber());
    }
    @Test
    void findAccountByIdSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.findById(1L)).thenReturn(Mono.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);

        Mono<Account> result = accountJpaAdapter.findById(1L);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.block().getAccountNumber());
    }
    @Test
    void deleteAccountByIdSuccessfully() {
        when(accountRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = accountJpaAdapter.deleteById(1L);

        assertNotNull(result);
        assertEquals(Mono.empty(), result);
    }
    @Test
    void findAllAccountsSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.findAll()).thenReturn(Flux.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);

        Flux<Account> result = accountJpaAdapter.findAll();

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.blockFirst().getAccountNumber());
    }
    @Test
    void getAccountInformationSuccessfully() {
        AccountEntity accountEntity = getAccount();
        Account account = getAccountEntity();
        when(accountRepository.findAccountByAccountNumber("2901020304")).thenReturn(Mono.just(accountEntity));
        when(modelMapper.map(accountEntity, Account.class)).thenReturn(account);

        Mono<Account> result = accountJpaAdapter.getAccountInformation("2901020304");

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.block().getAccountNumber());
    }

    private AccountEntity getAccount(){

        AccountEntity accountDTO =new AccountEntity();
        accountDTO.setIdAccount(Long.valueOf("1"));
        accountDTO.setAccountType("Ahorros");
        accountDTO.setAccountNumber("2901020304");
        accountDTO.setStatus(true);
        accountDTO.setInitialBalance(new BigDecimal(2000));

        return accountDTO;
    }
    private  Account  getAccountEntity(){

        Account account = new Account();
        account.setIdAccount(Long.valueOf("1"));
        account.setAccountType("Ahorros");
        account.setAccountNumber("2901020304");
        account.setStatus(true);
        account.setInitialBalance(new BigDecimal(2000));

        return account;
    }
}
