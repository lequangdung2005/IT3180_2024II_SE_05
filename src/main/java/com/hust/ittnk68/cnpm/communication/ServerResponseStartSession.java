package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerResponseStartSession extends ServerResponseBase
{
    String token;
    AccountType accountType;

    public ServerResponseStartSession (ResponseStatus status, String message, String token, AccountType accountType)
    {
        super (status, message);
        this.token = token;
        this.accountType = accountType;
    }

    public String getToken ()
    { 
        return token;
    }
    public void setToken (String token)
    {
        this.token = token;
    }

    public AccountType getAccountType ()
    {
        return accountType;
    }
    public void setAccountType (AccountType accountType)
    {
        this.accountType = accountType;
    }
}
