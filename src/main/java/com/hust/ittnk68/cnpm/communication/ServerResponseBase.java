package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerResponseBase
{
    private ResponseStatus responseStatus;
    private String responseMessage;

    public ServerResponseBase (ResponseStatus responseStatus, String responseMessage)
    {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }

    public ResponseStatus getResponseStatus ()
    {
        return responseStatus;
    }
    public void setResponseStatus (ResponseStatus responseStatus)
    {
        this.responseStatus = responseStatus;
    }

    public String getResponseMessage ()
    {
        return responseMessage;
    }
    public void setResponseMessage (String responseMessage)
    {
        this.responseMessage = responseMessage;
    }
}
