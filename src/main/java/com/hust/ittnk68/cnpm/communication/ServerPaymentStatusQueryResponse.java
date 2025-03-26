package com.hust.ittnk68.cnpm.communication;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerPaymentStatusQueryResponse extends ServerResponseBase {

    private List<Map<String, Object>> result;

    @JsonIgnore
    public ServerPaymentStatusQueryResponse (ResponseStatus responseStatus, String responseMessage) {
        super (responseStatus, responseMessage);
    }

    public ServerPaymentStatusQueryResponse (ResponseStatus responseStatus, String responseMessage, List<Map<String, Object>> result) {
        super (responseStatus, responseMessage);
        this.result = result;
    }

    public List<Map<String, Object>> getResult () {
        return result;
    }
    public void setResult (List<Map<String, Object>> result) {
        this.result = result;
    }
}
