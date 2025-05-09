package com.hust.ittnk68.cnpm.communication;

public class ClientMessageStartSession
{
    String username;
    String digestPassword;

    public ClientMessageStartSession (String username, String digestPassword)
    {
        this.username = username;
        this.digestPassword = digestPassword;
    }

    public String getUsername ()
    {
        return username;
    }
    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getDigestPassword ()
    {
        return digestPassword;
    }
    public void setDigestPassword (String digestPassword)
    {
        this.digestPassword = digestPassword;
    }
}
