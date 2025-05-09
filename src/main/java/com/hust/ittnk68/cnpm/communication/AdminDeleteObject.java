package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class AdminDeleteObject extends ClientMessageBase {

    private GetSQLProperties object;

    public AdminDeleteObject (String username, GetSQLProperties object) {
        super (username);
        this.object = object;
    } 

    public String getCondition () {
        return String.format ("%s_id='%d'", object.getSQLTableName(),
                                            object.getId());
    }

    public GetSQLProperties getObject() {
        return object;
    }
    public void setObject (GetSQLProperties object) {
        this.object = object;
    }
}
