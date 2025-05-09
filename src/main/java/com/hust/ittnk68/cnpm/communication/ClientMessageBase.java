package com.hust.ittnk68.cnpm.communication;

public class ClientMessageBase
{
    private String username; 

    ClientMessageBase (String username)
    {
        this.username = username;
    }

    public String getUsername ()
    {
        return username;
    }
    public void setUsername (String username)
    {
        this.username = username;
    }
}
