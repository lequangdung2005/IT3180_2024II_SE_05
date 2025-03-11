package com.hust.ittnk68.cnpm.type;

public enum ExpenseType {
    SERVICE("service"),
    ELECTRIC("electric"),
    WATER("water"),
    VEHICLE_PARK("parking");

    private String expenseType;
    ExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    @Override
    public String toString() {
        return expenseType;
    }
}
