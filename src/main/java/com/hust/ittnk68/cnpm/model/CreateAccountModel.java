package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateAccountModel {
    
    private final StringProperty familyId;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty password2;
    private final StringProperty accountType;

    public CreateAccountModel () {
        familyId = new SimpleStringProperty ();
        username = new SimpleStringProperty ();
        password = new SimpleStringProperty ();
        password2 = new SimpleStringProperty ();
        accountType = new SimpleStringProperty (); 
    }

    public StringProperty familyIdProperty () {
        return familyId;
    }
    public StringProperty usernameProperty () {
        return username;
    }
    public StringProperty passwordProperty () {
        return password;
    }
    public StringProperty password2Property () {
        return password2;
    }
    public StringProperty accountTypeProperty () {
        return accountType;
    }

}
