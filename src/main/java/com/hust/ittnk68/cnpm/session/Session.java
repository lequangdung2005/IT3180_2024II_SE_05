package com.hust.ittnk68.cnpm.session;

import java.util.Calendar;
import java.util.Date;

public class Session
{
    private String username;
    private Date sessionEnd;

    public Session (String username)
    {
        this.username = username;

        Date currentDate = new Date ();
        Calendar cal = Calendar.getInstance ();
        cal.setTime (currentDate);
        cal.add (Calendar.HOUR, 1);

        // session lasts one hour
        this.sessionEnd = cal.getTime ();
    }

    public Date getSessionEnd ()
    {
        return sessionEnd;
    }

}
