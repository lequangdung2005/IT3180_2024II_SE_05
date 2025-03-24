package com.hust.ittnk68.cnpm.communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerDeleteObjectResponse extends ServerResponseBase {

    int affectedRows;

    @JsonIgnore
    public ServerDeleteObjectResponse (ResponseStatus responseStatus, String responseMessage) {
        this (responseStatus, responseMessage, -1);
    }
    public ServerDeleteObjectResponse (ResponseStatus responseStatus, String responseMessage, int affectedRows) {
        super (responseStatus, responseMessage);
        this.affectedRows = affectedRows;
    }

    public void setAffectedRows (int affectedRows) {
        this.affectedRows = affectedRows;
    }
    public int getAffectedRows () {
        return affectedRows;
    }

}
