package com.bank.customeraccount;

import lombok.Getter;


public enum AccountType {
    S("savings"),
    C("checking");

    public String getValue() {
        return value;
    }

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

}
