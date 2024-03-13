package com.bank.customeraccount;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "Customer")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerMobile;

    @Column(nullable = false)
    @NotNull
    private String customerEmail;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Enumerated(EnumType.STRING)
    @Column(name="account_type", length=1, nullable=false)
    private AccountType accountType;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private Account account;

}