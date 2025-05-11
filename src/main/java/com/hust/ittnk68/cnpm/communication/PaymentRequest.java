package com.hust.ittnk68.cnpm.communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest extends ClientMessageBase
{

    private int paymentStatusId;
    private int amount;

    @JsonCreator
    public PaymentRequest (
        @JsonProperty("username") String username,
        @JsonProperty("paymentStatusId") int paymentStatusId,
        @JsonProperty("amount") int amount
    )
    {
        super (username);
        this.paymentStatusId = paymentStatusId;
        this.amount = amount;
    }

    public int getPaymentStatusId ()
    {
        return paymentStatusId;
    }
    public void setPaymentStatusId (int paymentStatusId)
    {
        this.paymentStatusId = paymentStatusId;
    }

    public int getAmount ()
    {
        return amount;
    }
    public void setAmount (int amount)
    {
        this.amount = amount;
    }

}
