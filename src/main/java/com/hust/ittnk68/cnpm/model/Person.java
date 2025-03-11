package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.type.*;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Person extends GetSQLProperties {
    private int personId;
    private int familyId;
    private String fullname;
    private Date dateOfBirth;
    private String citizenIdentificationNumber;
    private String phoneNumber;
    private Sex sex;
    private Nation nationality;
    private ResidenceStatus residenceStatus;

    public Person() {}
    public Person(int familyId, String fullname, Date dateOfBirth, String citizenIdentificationNumber,
            String phoneNumber, Sex sex, Nation nationality, ResidenceStatus residenceStatus)
    {
        this.personId = -1;
        this.familyId = familyId;
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
        this.citizenIdentificationNumber = citizenIdentificationNumber;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.nationality = nationality;
        this.residenceStatus = residenceStatus;
    }

    @Override
    public void setId(int id) {
        this.personId = id;
    }
    @Override
    public String getSQLTableName() {
        return new String("person");
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (family_id,fullname,date_of_birth,citizen_identification_number,phone_number,sex,nationality,residence_status) values ('%d','%s','%s','%s','%s','%s','%s','%s');", this.getSQLTableName(), familyId, fullname, dateOfBirth, citizenIdentificationNumber, phoneNumber, sex, nationality, residenceStatus);
    }
}
