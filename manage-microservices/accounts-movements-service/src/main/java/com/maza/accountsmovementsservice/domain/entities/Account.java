package com.maza.accountsmovementsservice.domain.entities;

import lombok.Data;

import java.math.BigDecimal;
/**
 * Account Entity.
 */
@Data
public class Account {
    public Long idAccount;
    public Long idCustomer;
    public String accountNumber;
    public String accountType;
    public BigDecimal initialBalance;
    public boolean status;
    public String customer;


}
