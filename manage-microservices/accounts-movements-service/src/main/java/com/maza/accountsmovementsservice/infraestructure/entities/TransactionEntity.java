package com.maza.accountsmovementsservice.infraestructure.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Transaction Entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ttransactions")
public class TransactionEntity {

    @Id
    private Long idTransaction;
    private Long idAccount;
    private LocalDate date;
    private String transactionType;
    private BigDecimal value;
    private BigDecimal balance;
}
