package com.hust.ittnk68.cnpm.session;

import java.util.Calendar;
import java.util.Date;

import com.hust.ittnk68.cnpm.type.AccountType;

public class Session
{
    private String username;
    private AccountType accountType;
    private Date sessionEnd;

    public Session (String username, AccountType accountType)
    {
        this.username = username;
        this.accountType = accountType;

        Date currentDate = new Date ();
        Calendar cal = Calendar.getInstance ();
        cal.setTime (currentDate);
        cal.add (Calendar.HOUR, 1);

        // session lasts one hour
        this.sessionEnd = cal.getTime ();
    }

    public String getUsername () {
        return username;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public Date getSessionEnd ()
    {
        return sessionEnd;
    }

}
