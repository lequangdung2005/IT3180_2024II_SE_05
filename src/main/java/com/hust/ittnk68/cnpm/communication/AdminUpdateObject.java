package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class AdminUpdateObject extends ClientMessageBase {

    private GetSQLProperties object;

    public AdminUpdateObject (String username, GetSQLProperties object) {
        super (username);
        this.object = object;
    } 

    public GetSQLProperties getObject() {
        return object;
    }
    public void setObject (GetSQLProperties object) {
        this.object = object;
    }

}
