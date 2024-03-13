package com.bank.customeraccount;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponse {
    private long customerNumber;
    private int transactionStatusCode;
    private String transactionStatusDescription;
}