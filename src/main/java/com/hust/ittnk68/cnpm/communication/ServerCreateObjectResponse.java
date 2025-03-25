package com.hust.ittnk68.cnpm.communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerCreateObjectResponse extends ServerResponseBase {

    private GetSQLProperties object;
    
    @JsonIgnore
    public ServerCreateObjectResponse (ResponseStatus responseStatus, String responseMessage)
    {
        this (responseStatus, responseMessage, null);
    }
    public ServerCreateObjectResponse (ResponseStatus responseStatus, String responseMessage, GetSQLProperties objectId)
    {
        super (responseStatus, responseMessage);
        this.object = objectId;
    }

    public GetSQLProperties getObjectId () {
        return object;
    }
    public void setObjectId (GetSQLProperties objectId) {
        this.object = objectId;
    }
}
