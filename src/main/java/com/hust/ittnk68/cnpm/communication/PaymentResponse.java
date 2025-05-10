package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class PaymentResponse extends ServerResponseBase {

    public PaymentResponse (ResponseStatus responseStatus, String responseMessage)
    {
        super (responseStatus, responseMessage);
    }

}

