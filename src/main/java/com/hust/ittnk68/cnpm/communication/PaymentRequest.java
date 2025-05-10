package com.hust.ittnk68.cnpm.communication;

public class PaymentRequest extends ClientMessageBase
{

    private int paymentStatusId;
    private int amount;

    public PaymentRequest (String username, int paymentStatusId, int amount)
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
