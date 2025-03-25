package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;

public class FindAccountModel {
    
    private final StringProperty accountId;
    private final StringProperty familyId;
    private final StringProperty username;
    private final StringProperty digestPassword;
    private final StringProperty accountType;

    private TableView<Account> tableView;

    public FindAccountModel () {
        accountId = new SimpleStringProperty ();
        familyId = new SimpleStringProperty ();
        username = new SimpleStringProperty ();
        digestPassword = new SimpleStringProperty ();
        accountType = new SimpleStringProperty (); 
    }

    public StringProperty accountIdProperty () {
        return accountId;
    }
    public StringProperty familyIdProperty () {
        return familyId;
    }
    public StringProperty usernameProperty () {
        return username;
    }
    public StringProperty digestPasswordProperty () {
        return digestPassword;
    }
    public StringProperty accountTypeProperty () {
        return accountType;
    }

    public void setTableView (TableView<Account> tableView) {
        this.tableView = tableView;
    }
    public TableView<Account> getTableView() {
        return tableView;
    }

}
