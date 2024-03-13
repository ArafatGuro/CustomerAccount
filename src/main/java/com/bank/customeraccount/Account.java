package com.bank.customeraccount;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "Account")
@Data
public class Account {

    @Id
    @Column(name="account_number", nullable=false)
    private int accountNumber;

    @NotNull
    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name="available_balance", nullable=false)
    private double availableBalance;

    @OneToOne
    @JoinColumn(name = "customerId")
    private Customer customerAccount;
}