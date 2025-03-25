package com.hust.ittnk68.cnpm.model;

import org.apache.commons.codec.digest.*;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.AccountType;

public class Account extends GetSQLProperties {
    private int accountId;
    private int familyId;
    private String username;
    private String digestPassword;
    private AccountType accountType;

    @JsonIgnore
    public Account () {}

    public Account(int familyId, String username, String digestPassword, AccountType accountType) {
        this.accountId = -1;
        this.familyId = familyId;
        this.username = username;
        this.digestPassword = digestPassword;
        this.accountType = accountType;
    }

    @JsonIgnore
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

    public void setAccountId (int accountId) {
        this.accountId = accountId;
    }
    public int getAccountId() {
        return accountId;
    }

    public void setFamilyId (int familyId) {
        this.familyId = familyId;
    }
    public int getFamilyId() {
        return familyId;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setDigestPassword (String digestPassword) {
        this.digestPassword = digestPassword;
    }
    public String getDigestPassword() {
        return digestPassword;
    }

    public void setAccountType (AccountType accountType) {
        this.accountType = accountType;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    @JsonIgnore
    public static Account convertFromMap (Map<String, Object> map) {
        Account acc = new Account (
            (int)map.get("family_id"),
            (String)map.get("username"),
            (String)map.get("digest_password"),
            AccountType.matchByString((String)map.get("account_type")).get()
        );
        acc.setId ((int)map.get("account_id"));
        return acc;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return accountId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        this.accountId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return new String("account");
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (family_id,username,digest_password,account_type) values ('%d','%s','%s','%s');", this.getSQLTableName(), familyId, username, digestPassword, accountType);
    }
    @Override
    @JsonIgnore
    public String getSQLUpdateCommand() {
        return String.format("UPDATE %s SET family_id='%d',username='%s',digest_password='%s',account_type='%s' WHERE %s_id='%d';",
                                this.getSQLTableName(),
                                familyId, username, digestPassword, accountType,
                                this.getSQLTableName(), getId());
    }
}
