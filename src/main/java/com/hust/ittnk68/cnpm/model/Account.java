package com.hust.ittnk68.cnpm.model;

import org.apache.commons.codec.digest.*;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.AccountType;

public class Account extends GetSQLProperties {
    private int accountId;
    private int familyId;
    private String username;
    private String digestPassword;
    private AccountType accountType;

    public Account() {}
    public Account(int familyId, String username, String password, String digestPassword, AccountType accountType) {
        this.accountId = -1;
        this.familyId = familyId;
        this.username = username;
        if(password != null) {
            this.digestPassword = DigestUtils.sha256Hex(password);
        }
        if(digestPassword != null) {
            this.digestPassword = digestPassword;
        }
        this.accountType = accountType;
    }

    public int getAccountId() {
        return accountId;
    }
    public int getFamilyId() {
        return familyId;
    }
    public String getUsername() {
        return username;
    }
    public String getDigestPassword() {
        return digestPassword;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setId(int id) {
        this.accountId = id;
    }
    @Override
    public String getSQLTableName() {
        return new String("account");
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (family_id,username,digest_password,account_type) values ('%d','%s','%s','%s');", this.getSQLTableName(), familyId, username, digestPassword, accountType);
    }
}
