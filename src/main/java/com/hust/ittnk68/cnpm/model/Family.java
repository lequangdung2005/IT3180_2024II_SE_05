package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Family extends GetSQLProperties {
    private int familyId;
    private int personId;
    private String houseNumber;

    public Family(int personId, String houseNumber) {
        this.familyId = -1;
        this.personId = personId;
        this.houseNumber = houseNumber;
    }

    @Override
    public void setId(int id) {
        familyId = id;
    }
    @Override
    public String getSQLTableName() {
        return "family";
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (person_id,house_number) values ('%d','%s');", this.getSQLTableName(), personId, houseNumber);
    }
}
