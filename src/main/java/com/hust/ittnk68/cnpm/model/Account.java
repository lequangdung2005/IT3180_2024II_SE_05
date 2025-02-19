package com.hust.ittnk68.cnpm.model;

import org.apache.commons.codec.digest.*;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class Account implements GetSQLProperties {
    private int accountId;
    private int familyId;
    private String username;
    private String digestPassword;

    // tim kiem trong database
    public Account(int accountId) {
        this.accountId = accountId;
    }
    // tao cai moi
    public Account(int familyId, String username, String password) { 
        this.familyId = familyId;
        this.username = username;
        // this.digestPassword = ? (password);
    }

    public String getSQLTableName() {
        return new String("account_id");
    }
    public String getSQLInsertColumns() {
        return new String("familyId, username, digestPassword");
    }
    public String getSQLInsertValues() {
        return String.format("%d, %s, %s", familyId, username, digestPassword);
    }
}
