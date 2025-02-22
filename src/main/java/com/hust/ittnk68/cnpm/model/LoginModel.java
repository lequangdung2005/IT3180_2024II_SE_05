package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginModel {
    private final StringProperty username;
    private final StringProperty password;

    private final StringProperty loginMessageFill;
    private final StringProperty loginMessageContent;

    public LoginModel() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        loginMessageFill = new SimpleStringProperty();
        loginMessageContent = new SimpleStringProperty();
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

    public StringProperty getLoginMessageFillProperty() {
        return loginMessageFill;
    }
    public String getLoginMessageFill() {
        return loginMessageFill.get();
    }
    public void setLoginMessageFill(String loginMessageFill) {
        this.loginMessageFill.set(loginMessageFill);
    }

    public StringProperty getLoginMessageContentProperty() {
        return loginMessageContent;
    }
    public String getLoginMessageContent() {
        return loginMessageContent.get();
    }
    public void setLoginMessageContent(String loginMessageContent) {
        this.loginMessageContent.set(loginMessageContent);
    }
}
