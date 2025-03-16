package com.hust.ittnk68.cnpm.communication;

public class ClientMessageBase
{
    private String token; 

    ClientMessageBase (String token)
    {
        this.token = token;
    }

    public String getToken ()
    {
        return token;
    }
    public void setToken (String token)
    {
        this.token = token;
    }
}
