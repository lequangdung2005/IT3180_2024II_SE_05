package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreatePaymentStatusModel {
    
    private final StringProperty paymentStatusId;
    private final StringProperty expenseId;
    private final StringProperty familyId;
    private final StringProperty totalPay;
    private final ObjectProperty<LocalDate> publishedDate;

    public CreatePaymentStatusModel () {
        paymentStatusId = new SimpleStringProperty ();
        expenseId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
        totalPay = new SimpleStringProperty ();
        publishedDate = new SimpleObjectProperty<> ();
    } 

    public StringProperty paymentStatusIdProperty () {
        return paymentStatusId;
    }
    public StringProperty expenseIdProperty () {
        return expenseId;
    }
    public StringProperty familyIdProperty () {
        return familyId;
    }
    public StringProperty totalPayProperty () {
        return totalPay;
    }
    public ObjectProperty<LocalDate> publishedDateProperty () {
        return publishedDate;
    }

}
