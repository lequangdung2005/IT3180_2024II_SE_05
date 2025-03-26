package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class UserQueryObjectById extends ClientMessageBase {

    GetSQLProperties object;

    public UserQueryObjectById (String token, GetSQLProperties object) {
        super (token);
        this.object = object;
    } 

    public GetSQLProperties getObject () {
        return object;
    }
    public void setObject (GetSQLProperties object) {
        this.object = object;
    }

}
