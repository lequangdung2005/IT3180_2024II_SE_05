package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class AdminUpdateObject extends ClientMessageBase {

    private String operation;
    private GetSQLProperties object;

    public AdminUpdateObject (String token, String operation, GetSQLProperties object) {
        super (token);
        this.operation = operation;
        this.object = object;
    } 

    public String getOperation () {
        return operation;
    }
    public void setOperation (String operation) {
        this.operation = operation;
    }

    public GetSQLProperties getObject() {
        return object;
    }
    public void setObject (GetSQLProperties object) {
        this.object = object;
    }

}
