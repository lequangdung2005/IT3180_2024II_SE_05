package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class AdminFindObject extends ClientMessageBase {

    private String condition;
    private GetSQLProperties object;

    public AdminFindObject (String token, String condition, GetSQLProperties object) {
        super (token);
        this.condition = condition;
        this.object = object;
    } 

    public String getCondition () {
        return condition;
    }
    public void setCondition (String condition) {
        this.condition = condition;
    }

    public GetSQLProperties getObject() {
        return object;
    }
    public void setObject (GetSQLProperties object) {
        this.object = object;
    }

}
