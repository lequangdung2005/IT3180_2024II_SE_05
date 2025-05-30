package com.hust.ittnk68.cnpm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreatePersonModel {
    
    private final StringProperty fullname;
    private ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty citizenId;
    private final StringProperty phoneNumber;
    private final StringProperty sex;
    private final StringProperty nation;
    private final StringProperty residenceStatus;
    private final StringProperty familyId;

    public CreatePersonModel () {
        fullname = new SimpleStringProperty ();
        dateOfBirth = new SimpleObjectProperty<> ();
        citizenId = new SimpleStringProperty ();
        phoneNumber = new SimpleStringProperty ();
        sex = new SimpleStringProperty ();
        nation = new SimpleStringProperty ();
        residenceStatus = new SimpleStringProperty ();
        familyId = new SimpleStringProperty();
    }

    public StringProperty getFullnameProperty() {
        return fullname;
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
    public StringProperty getFamilyIdProperty() {
        return familyId;
    }

}
