package com.hust.ittnk68.cnpm.communication;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class ServerPaymentStatusQueryResponse extends ServerResponseBase {

    private List<PaymentStatus> result;

    @JsonIgnore
    public ServerPaymentStatusQueryResponse (ResponseStatus responseStatus, String responseMessage) {
        super (responseStatus, responseMessage);
    }

    public ServerPaymentStatusQueryResponse (ResponseStatus responseStatus, String responseMessage, List<PaymentStatus> result) {
        super (responseStatus, responseMessage);
        this.result = result;
    }

    public List<PaymentStatus> getResult () {
        return result;
    }
    public void setResult (List<PaymentStatus> result) {
        this.result = result;
    }
}
