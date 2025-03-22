package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.Account;

public class AdminCreateAccount extends ClientMessageBase {

    Account account;

    public AdminCreateAccount (String token, Account account) {
        super (token);
        this.account = account;
    }

    public Account getAccount () {
        return account;
    }
    public void setAccount (Account account) {
        this.account = account;
    }

}
