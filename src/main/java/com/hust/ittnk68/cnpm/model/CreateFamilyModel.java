package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateFamilyModel {

    private final StringProperty personId;
    private final StringProperty houseNumber;

    public CreateFamilyModel () {
        personId = new SimpleStringProperty ();
        houseNumber = new SimpleStringProperty ();
    }

    public StringProperty personIdProperty () {
        return personId;
    }
    public StringProperty houseNumberProperty () {
        return houseNumber;
    }

}
