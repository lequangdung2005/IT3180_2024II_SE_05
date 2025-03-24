package com.hust.ittnk68.cnpm.communication;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerFindObjectResponse extends ServerResponseBase {

    private List<Map<String, Object>> requestResult; 
    
    @JsonIgnore
    public ServerFindObjectResponse (ResponseStatus responseStatus, String responseMessage) {
        this (responseStatus, responseMessage, null);
    }

    public ServerFindObjectResponse (ResponseStatus responseStatus, String responseMessage, List<Map<String, Object>> requestResult) {
        super (responseStatus, responseMessage);
        this.requestResult = requestResult;
    }

    public List<Map<String, Object>> getRequestResult () {
        return requestResult;
    }
    public void setRequestResult (List<Map<String, Object>> requestResult) {
        this.requestResult = requestResult;
    }

}
