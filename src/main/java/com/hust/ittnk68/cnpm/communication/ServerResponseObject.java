package com.hust.ittnk68.cnpm.communication;

import java.util.Map;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerResponseObject extends ServerResponseBase {

    Map<String, Object> result;

    public ServerResponseObject(ResponseStatus responseStatus, String responseMessage, Map<String, Object> result) {
        super(responseStatus, responseMessage);
        this.result = result;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
    
}
