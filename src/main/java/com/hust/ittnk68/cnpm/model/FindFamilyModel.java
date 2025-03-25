package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;

public class FindFamilyModel {
    
    private final StringProperty familyId;
    private final StringProperty personId;
    private final StringProperty houseNumber;

    private TableView<Family> tableView;

    public FindFamilyModel () {
        familyId = new SimpleStringProperty ();
        personId = new SimpleStringProperty ();
        houseNumber = new SimpleStringProperty ();
    }

    public StringProperty personIdProperty () {
        return personId;
    }
    public StringProperty familyIdProperty () {
        return familyId;
    }
    public StringProperty houseNumberProperty() {
        return houseNumber;
    }

    public void setTableView (TableView<Family> tableView) {
        this.tableView = tableView;
    }
    public TableView<Family> getTableView() {
        return tableView;
    }

}
