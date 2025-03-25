package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class UpdateFamilyModel {
    
    private final StringProperty familyId;
    private final StringProperty personId;
    private final StringProperty houseNumber;

    private Stage updateFamilyWindow;

    public UpdateFamilyModel () {
        personId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
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

    public Stage getUpdateFamilyWindow () {
        return updateFamilyWindow;
    }
    public void setUpdateFamilyWindow (Stage updateFamilyWindow) {
        this.updateFamilyWindow = updateFamilyWindow;
    }

}
