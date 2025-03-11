package com.hust.ittnk68.cnpm.type;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
    ROOT("root"),
    ADMIN("admin"),
    USER("user"),
    UNVALID("unvalid");

    private String accountType;
    AccountType(String accountType) {
        this.accountType = accountType;
    }

    public static Optional<AccountType> matchByString(String name) {
        return Arrays.stream(values()).filter(it -> it.toString().equals(name)).findAny();
    }

    @Override
    public String toString() {
        return accountType;
    }
}
