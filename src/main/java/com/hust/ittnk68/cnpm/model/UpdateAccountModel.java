package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class UpdateAccountModel {
    
    private final StringProperty accountId;
    private final StringProperty familyId;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty password2;
    private final StringProperty accountType;

    private String digestPassword;

    private Stage updateAccountWindow;

    public UpdateAccountModel () {
        accountId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
        username = new SimpleStringProperty ();
        password = new SimpleStringProperty ();
        password2 = new SimpleStringProperty ();
        accountType = new SimpleStringProperty (); 
    }

    public StringProperty accountIdProperty() {
        return accountId;
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

    public String getDigestPassword () {
        return digestPassword;
    }
    public void setDigestPassword (String digestPassword) {
        this.digestPassword = digestPassword;
    }

    public Stage getUpdateAccountWindow () {
        return updateAccountWindow;
    }
    public void setUpdateAccountWindow (Stage updateAccountWindow) {
        this.updateAccountWindow = updateAccountWindow;
    }

}
