package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;

public class FindPersonModel {
    
    private final StringProperty personId;
    private final StringProperty familyId;
    private final StringProperty fullname;
    private BooleanProperty enableDateOfBirth;
    private ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty citizenId;
    private final StringProperty phoneNumber;
    private final StringProperty sex;
    private final StringProperty nation;
    private final StringProperty residenceStatus;

    private TableView<Person> tableView;

    public FindPersonModel () {
        personId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
        fullname = new SimpleStringProperty ();
        enableDateOfBirth = new SimpleBooleanProperty ();
        dateOfBirth = new SimpleObjectProperty<> ();
        citizenId = new SimpleStringProperty ();
        phoneNumber = new SimpleStringProperty ();
        sex = new SimpleStringProperty ();
        nation = new SimpleStringProperty ();
        residenceStatus = new SimpleStringProperty ();
    }

    public StringProperty getPersonIdProperty () {
        return personId;
    }
    public StringProperty getFamilyIdProperty () {
        return familyId;
    }
    public StringProperty getFullnameProperty() {
        return fullname;
    }
    public BooleanProperty getEnableDateOfBirth () {
        return enableDateOfBirth;
    }
    public ObjectProperty<LocalDate> getDateOfBirthProperty () {
        return dateOfBirth;
    }
    public StringProperty getCitizenIdProperty () {
        return citizenId;
    }
    public StringProperty getPhoneNumberProperty () {
        return phoneNumber;
    }
    public StringProperty getSexProperty () {
        return sex;
    }
    public StringProperty getNationProperty () {
        return nation;
    }
    public StringProperty getResidenceStatusProperty () {
        return residenceStatus;
    }

    public void setTableView (TableView<Person> tableView) {
        this.tableView = tableView;
    }
    public TableView<Person> getTableView() {
        return tableView;
    }

}
