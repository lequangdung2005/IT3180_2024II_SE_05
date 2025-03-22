package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.Expense;

public class AdminCreateExpense extends ClientMessageBase {

    Expense expense;

    public AdminCreateExpense (String token, Expense expense) {
        super (token);
        this.expense = expense;
    }

    public Expense getExpense () {
        return expense;
    }
    public void setExpense (Expense e) {
        this.expense = e;
    }

}
