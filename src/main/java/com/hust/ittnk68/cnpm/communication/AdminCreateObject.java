package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class AdminCreateObject extends ClientMessageBase {

    GetSQLProperties object;

    public AdminCreateObject (String username, GetSQLProperties object) {
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
