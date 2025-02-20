package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Expense extends GetSQLProperties {
    private int expenseId;
    private String expenseTitle;
    private String expenseDescription;
    private int totalCost;
    private boolean required;

    public Expense(String expenseTitle, String expenseDescription, int totalCost, boolean required) {
        this.expenseId = -1;
        this.expenseTitle = expenseTitle;
        this.expenseDescription = expenseDescription;
        this.totalCost = totalCost;
        this.required = required;
    }

    @Override
    public void setId(int id) {
        this.expenseId = id;
    }
    @Override
    public String getSQLTableName() {
        return new String("expense");
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (expense_title,expense_description,total_cost,required) values ('%s','%s','%d','%d');", this.getSQLTableName(), expenseTitle, expenseDescription, totalCost, required ? 1 : 0);
    }
}
