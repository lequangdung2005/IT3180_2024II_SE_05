package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.type.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public int getPersonId () {
        return personId;
    }
    public void setPersonId (int personId) {
        this.personId = personId;
    }

    public int getFamilyId () {
        return familyId;
    }
    public void setFamilyId (int familyId) {
        this.familyId = familyId;
    }

    public String getFullname () {
        return fullname;
    }
    public void setFullname (String fullname) {
        this.fullname = fullname;
    }

    public Date getDateOfBirth () {
        return dateOfBirth;
    }
    public void setDateOfBirth (Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCitizenIdentificationNumber () {
        return citizenIdentificationNumber;
    }
    public void setCitizenIdentificationNumber (String citizenIdentificationNumber) {
        this.citizenIdentificationNumber = citizenIdentificationNumber;
    }

    public String getPhoneNumber () {
        return phoneNumber;
    }
    public void setPhoneNumber (String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Sex getSex () {
        return sex;
    }
    public void setSex (Sex sex) {
        this.sex = sex;
    }

    public Nation getNationality () {
        return nationality;
    }
    public void setNationality (Nation nationality) {
        this.nationality = nationality;
    }

    public ResidenceStatus getResidenceStatus () {
        return residenceStatus;
    }
    public void setResidenceStatus (ResidenceStatus residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return personId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        this.personId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return new String("person");
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (family_id,fullname,date_of_birth,citizen_identification_number,phone_number,sex,nationality,residence_status) values ('%d','%s','%s','%s','%s','%s','%s','%s');", this.getSQLTableName(), familyId, fullname, dateOfBirth, citizenIdentificationNumber, phoneNumber, sex, nationality, residenceStatus);
    }
}
