package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

import com.hust.ittnk68.cnpm.type.Date;

public class PaymentStatus extends GetSQLProperties {
    private int paymentStatusId;
    private int expenseId;
    private int familyId;
    private int totalPay;
    private Date publishedDate;

    public PaymentStatus() {}
    public PaymentStatus(int expenseId, int familyId, int totalPay) {
        this.paymentStatusId = -1;
        this.expenseId = expenseId;
        this.familyId = familyId;
        this.totalPay = totalPay;
    }

    @Override
    public void setId(int id) {
        paymentStatusId = id;
    }
    @Override
    public String getSQLTableName() {
        return new String("payment_status");
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (expense_id,family_id,total_pay,published_date) values ('%d','%d','%d','%s');", this.getSQLTableName(), expenseId, familyId, totalPay, publishedDate);
    }
}
