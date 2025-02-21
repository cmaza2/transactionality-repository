package com.maza.accountsmovementsservice.infraestructure.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "taccounts")
public class AccountEntity {
    @Id
    public Long idAccount;

    public Long idCustomer;
    public String accountNumber;
    public String accountType;
    public BigDecimal initialBalance;
    public boolean status;


}
