package com.bank.customeraccount;

import lombok.Getter;

@Getter
public enum AccountType {
    S("savings"),
    C("checking");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

}
