package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginModel {
    private final StringProperty username;
    private final StringProperty password;

    public LoginModel() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
    }

    public StringProperty getUsernameProperty() {
        return username;
    }
    public String getUsername() {
        return username.get();
    }
    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty getPasswordProperty() {
        return password;
    }
    public String getPassword() {
        return password.get();
    }
    public void setPassword(String password) {
        this.password.set(password);
    }
}
