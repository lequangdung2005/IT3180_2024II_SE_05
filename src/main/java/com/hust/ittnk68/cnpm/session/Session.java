package com.hust.ittnk68.cnpm.session;

import java.util.Calendar;
import java.util.Date;

import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.type.AccountType;

public class Session
{
    Account account;
    private Date sessionEnd;

    public Session (Account account, AccountType accountType)
    {
        this.account = account;

        Date currentDate = new Date ();
        Calendar cal = Calendar.getInstance ();
        cal.setTime (currentDate);
        cal.add (Calendar.HOUR, 1);

        // session lasts one hour
        this.sessionEnd = cal.getTime ();
    }

    public Account getAccount () {
        return account;
    }
    public Date getSessionEnd ()
    {
        return sessionEnd;
    }

}
