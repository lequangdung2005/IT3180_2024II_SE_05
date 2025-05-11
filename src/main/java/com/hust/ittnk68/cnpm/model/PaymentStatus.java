package com.hust.ittnk68.cnpm.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;

import com.hust.ittnk68.cnpm.type.Date;

public class PaymentStatus extends GetSQLProperties {
    private int paymentStatusId;
    private int expenseId;
    private int familyId;
    private int totalPay;
    private Date publishedDate;

    @JsonIgnore
    public PaymentStatus() {}

    public PaymentStatus(int expenseId, int familyId, int totalPay, Date publishedDate) {
        this.paymentStatusId = -1;
        this.expenseId = expenseId;
        this.familyId = familyId;
        this.totalPay = totalPay;
        this.publishedDate = publishedDate;
    }

    @JsonIgnore
    public static PaymentStatus convertFromMap (Map<String, Object> map) {
        PaymentStatus ps = new PaymentStatus (
            (int) map.get("expense_id"),
            (int) map.get("family_id"),
            (int) map.get("total_pay"),
            Date.cast ((String) map.get("published_date"))
        );
        ps.setId ((int)map.get("payment_status_id"));
        return ps;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return paymentStatusId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        paymentStatusId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return new String("payment_status");
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (expense_id,family_id,total_pay,published_date) values ('%d','%d','%d','%s');", this.getSQLTableName(), expenseId, familyId, totalPay, publishedDate);
    }
    @Override
    @JsonIgnore
    public String getSQLUpdateCommand() {
        return String.format("UPDATE %s SET expense_id='%d',family_id='%d',total_pay='%d',published_date='%s' WHERE %s_id='%d';",
                this.getSQLTableName(),
                expenseId, familyId, totalPay, publishedDate,
                this.getSQLTableName(), getId());
    }

	public int getPaymentStatusId() {
		return paymentStatusId;
	}

	public void setPaymentStatusId(int paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public int getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(int totalPay) {
		this.totalPay = totalPay;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
}
