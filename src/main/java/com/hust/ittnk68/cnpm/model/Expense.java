package com.hust.ittnk68.cnpm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.ExpenseType;

public class Expense extends GetSQLProperties {
    private int expenseId;
    private String expenseTitle;
    private String expenseDescription;
    private Date publishedDate;
    private int totalCost;
    private ExpenseType expenseType;
    private boolean required;

    public Expense() {}
    public Expense(String expenseTitle, String expenseDescription,
                    Date publishedDate, int totalCost,
                    ExpenseType expenseType, boolean required)
    {
        this.expenseId = -1;
        this.expenseTitle = expenseTitle;
        this.expenseDescription = expenseDescription;
        this.publishedDate = publishedDate;
        this.totalCost = totalCost;
        this.expenseType = expenseType;
        this.required = required;
    }

    public int getExpenseId () {
        return expenseId;
    }
    public String getExpenseTitle () {
        return expenseTitle;
    }
    public String getExpenseDescription () {
        return expenseDescription;
    }
    public Date getPublishedDate () {
        return publishedDate;
    }
    public int getTotalCost () {
        return totalCost;
    }
    public ExpenseType getExpenseType () {
        return expenseType;
    }
    public boolean getRequired () {
        return required;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return expenseId;
    }
    @JsonIgnore
    @Override
    public void setId(int id) {
        this.expenseId = id;
    }
    @JsonIgnore
    @Override
    public String getSQLTableName() {
        return new String("expense");
    }
    @JsonIgnore
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (expense_title,expense_description,published_date,total_cost,expense_type,required) values ('%s','%s','%s','%d','%s','%d');", this.getSQLTableName(), expenseTitle, expenseDescription, publishedDate, totalCost, required ? 1 : 0);
    }
}
