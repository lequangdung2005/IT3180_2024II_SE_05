package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.beans.property.BooleanProperty;

public class UpdateExpenseModel {
    
    private final StringProperty expenseId;
    private final StringProperty expenseTitle;
    private final StringProperty expenseDescription;
    private final ObjectProperty<LocalDate> publishedDate;
    private final StringProperty totalCost;
    private final StringProperty expenseType;
    private final BooleanProperty required;

    private Stage updateExpenseWindow;

    public UpdateExpenseModel () {
        expenseId = new SimpleStringProperty ();
        expenseTitle = new SimpleStringProperty ();
        expenseDescription = new SimpleStringProperty ();
        publishedDate = new SimpleObjectProperty<> ();
        totalCost = new SimpleStringProperty ();
        expenseType = new SimpleStringProperty ();
        required = new SimpleBooleanProperty ();
    }

    public StringProperty expenseIdProperty () {
        return expenseId;
    }
    public StringProperty expenseTitleProperty () {
        return expenseTitle;
    }
    public StringProperty expenseDescriptionProperty () {
        return expenseDescription;
    }
    public ObjectProperty<LocalDate> publishedDateProperty () {
        return publishedDate;
    }
    public StringProperty totalCostProperty () {
        return totalCost;
    }
    public StringProperty expenseTypeProperty () {
        return expenseType;
    }
    public BooleanProperty requiredProperty() {
        return required;
    }

    public Stage getUpdateExpenseWindow () {
        return updateExpenseWindow;
    }
    public void setUpdateExpenseWindow (Stage updateExpenseWindow) {
        this.updateExpenseWindow = updateExpenseWindow;
    }

}
