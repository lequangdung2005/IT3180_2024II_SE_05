package com.hust.ittnk68.cnpm.model;

import org.springframework.web.client.RestClient;

import com.hust.ittnk68.cnpm.model.ClientModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientModel {
    private final StringProperty username;
    private final StringProperty password;

    private String token;
    private String uriBase;
    private RestClient restClient;

    public ClientModel() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        restClient = RestClient.create ();
    }

    public RestClient getRestClient () {
        return restClient;
    }

    public void setUriBase (String u) {
        this.uriBase = u;
    }
    public String getUriBase () {
        return uriBase;
    }

    public void setToken (String t) {
        this.token = t;
    }
    public String getToken () {
        return this.token;
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
