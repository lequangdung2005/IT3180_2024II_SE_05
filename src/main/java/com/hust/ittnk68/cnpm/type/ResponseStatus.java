package com.hust.ittnk68.cnpm.type;

public enum ResponseStatus
{
    OK("ok"),
    CANT_CONNECT_SERVER("can't connect to server"),
    SESSION_ERROR("session_error");

    private String responseStatus;
    ResponseStatus (String responseStatus)
    {
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString ()
    {
        return responseStatus;
    }
}
