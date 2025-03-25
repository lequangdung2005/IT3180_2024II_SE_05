package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

public class CreateExpenseModel {
    
    private final StringProperty expenseTitle;
    private final StringProperty expenseDescription;
    private final ObjectProperty<LocalDate> publishedDate;
    private final StringProperty totalCost;
    private final StringProperty expenseType;
    private final BooleanProperty required;

    public CreateExpenseModel () {
        expenseTitle = new SimpleStringProperty ();
        expenseDescription = new SimpleStringProperty ();
        publishedDate = new SimpleObjectProperty<> ();
        totalCost = new SimpleStringProperty ();
        expenseType = new SimpleStringProperty ();
        required = new SimpleBooleanProperty ();
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

}
