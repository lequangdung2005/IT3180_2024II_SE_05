package com.hust.ittnk68.cnpm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Family extends GetSQLProperties {
    private int familyId;
    private int personId;
    private String houseNumber;

    public Family() {}
    public Family(int personId, String houseNumber) {
        this.familyId = -1;
        this.personId = personId;
        this.houseNumber = houseNumber;
    }

    public int getFamilyId () {
        return familyId;
    }
    public void setFamilyId (int familyId) {
        this.familyId = familyId;
    }

    public int getPersonId () {
        return personId;
    }
    public void setPersonId (int personId) {
        this.personId = personId;
    }

    public String getHouseNumber () {
        return houseNumber;
    }
    public void setHouseNumber (String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return familyId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        familyId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return "family";
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (person_id,house_number) values ('%d','%s');", this.getSQLTableName(), personId, houseNumber);
    }
}
