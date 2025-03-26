package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;

public class FindPaymentStatusModel {
    
    private final StringProperty paymentStatusId;
    private final StringProperty expenseId;
    private final StringProperty familyId;
    private final StringProperty totalPay;
    
    private final BooleanProperty enableDateFilter;
    private final ObjectProperty<LocalDate> beginPublishedDate;
    private final ObjectProperty<LocalDate> endPublishedDate;

    private TableView<PaymentStatus> tableView;

    public FindPaymentStatusModel () {
        paymentStatusId = new SimpleStringProperty ();
        expenseId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
        totalPay = new SimpleStringProperty ();
        enableDateFilter = new SimpleBooleanProperty ();
        beginPublishedDate = new SimpleObjectProperty<> ();
        endPublishedDate = new SimpleObjectProperty<> ();
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
    public BooleanProperty enableDateFilterProperty () {
        return enableDateFilter;
    }
    public ObjectProperty<LocalDate> beginPublishedDateProperty () {
        return beginPublishedDate;
    }
    public ObjectProperty<LocalDate> endPublishedDateProperty () {
        return endPublishedDate;
    }

    public void setTableView (TableView<PaymentStatus> tableView) {
        this.tableView = tableView;
    }
    public TableView<PaymentStatus> getTableView() {
        return tableView;
    }

}
