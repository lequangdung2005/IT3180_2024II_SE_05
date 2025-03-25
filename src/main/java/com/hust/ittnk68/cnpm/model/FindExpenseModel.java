package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.model.FindExpenseModel;
import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import javafx.beans.property.BooleanProperty;

public class FindExpenseModel {
    
    private final BooleanProperty enableDayFilter;
    private final ObjectProperty<LocalDate> beginPublishedDate;
    private final ObjectProperty<LocalDate> endPublishedDate;
    private final StringProperty minTotalCost;
    private final StringProperty maxTotalCost;
    private final StringProperty expenseType;
    private final BooleanProperty required;

    private TableView<Expense> tableView;

    public FindExpenseModel () {
        enableDayFilter = new SimpleBooleanProperty ();
        beginPublishedDate = new SimpleObjectProperty<> ();
        endPublishedDate = new SimpleObjectProperty<> ();
        minTotalCost = new SimpleStringProperty ();
        maxTotalCost = new SimpleStringProperty ();
        expenseType = new SimpleStringProperty ();
        required = new SimpleBooleanProperty ();
    }

    public BooleanProperty enableDayFilterProperty () {
        return enableDayFilter;
    }
    public ObjectProperty<LocalDate> beginPublishedDateProperty () {
        return beginPublishedDate;
    }
    public ObjectProperty<LocalDate> endPublishedDateProperty () {
        return endPublishedDate;
    }
    public StringProperty minTotalCostProperty () {
        return minTotalCost;
    }
    public StringProperty maxTotalCostProperty () {
        return maxTotalCost;
    }
    public StringProperty expenseTypeProperty () {
        return expenseType;
    }
    public BooleanProperty requiredProperty() {
        return required;
    }

    public void setTableView (TableView<Expense> tableView) {
        this.tableView = tableView;
    }
    public TableView<Expense> getTableView() {
        return tableView;
    }

}
