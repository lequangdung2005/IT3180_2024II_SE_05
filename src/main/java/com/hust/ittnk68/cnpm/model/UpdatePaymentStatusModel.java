package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class UpdatePaymentStatusModel {
    
    private final StringProperty paymentStatusId;
    private final StringProperty expenseId;
    private final StringProperty familyId;
    private final StringProperty totalPay;
    private final ObjectProperty<LocalDate> publishedDate;

    private Stage updatePaymentStatusWindow;

    public UpdatePaymentStatusModel () {
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

    public Stage getUpdatePaymentStatusWindow () {
        return updatePaymentStatusWindow;
    }
    public void setUpdatePaymentStatusWindow (Stage updatePaymentStatusWindow) {
        this.updatePaymentStatusWindow = updatePaymentStatusWindow;
    }

}
