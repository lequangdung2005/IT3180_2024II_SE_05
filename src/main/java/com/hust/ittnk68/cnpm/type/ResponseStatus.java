package com.hust.ittnk68.cnpm.type;

public enum ResponseStatus
{
    OK("ok"),
    CANT_CONNECT_SERVER("can't connect to server"),
    SESSION_ERROR("session error"),
    PERMISSION_ERROR("not have permission"),
    SQL_ERROR("operation not succeed due to a sql exception");

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
