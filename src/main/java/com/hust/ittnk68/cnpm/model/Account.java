package com.hust.ittnk68.cnpm.model;

import org.apache.commons.codec.digest.*;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Account extends GetSQLProperties {
    private int accountId;
    private int familyId;
    private String username;
    private String digestPassword;

    public Account(int familyId, String username, String password) {
        this.accountId = -1;
        this.familyId = familyId;
        this.username = username;
        this.digestPassword = DigestUtils.sha256Hex(password);
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
        return String.format("INSERT INTO %s (family_id,username,digest_password) values ('%d','%s','%s');", this.getSQLTableName(), familyId, username, digestPassword);
    }
}
