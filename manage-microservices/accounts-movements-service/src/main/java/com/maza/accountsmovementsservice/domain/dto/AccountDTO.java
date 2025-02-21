package com.maza.accountsmovementsservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    public Long idAccount;

    public Long idCustomer;
    public String accountNumber;
    public String accountType;
    public BigDecimal initialBalance;
    public boolean status;
    public String customer;
}
