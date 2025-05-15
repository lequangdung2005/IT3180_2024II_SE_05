package com.hust.ittnk68.cnpm.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

public class TemporaryStayAbsentRequest extends GetSQLProperties {

    private int temporaryStayAbsentRequestId;
    private int personId;
    private int familyId;
    private String fullname;
    private Date dateOfBirth;
    private String citizenIdentificationNumber;
    private String phoneNumber;
    private Sex sex;
    private Nation nationality;
    private ResidenceStatus residenceStatus;

    public TemporaryStayAbsentRequest () {}

    public TemporaryStayAbsentRequest(int personId, int familyId, String fullname,
            Date dateOfBirth, String citizenIdentificationNumber, String phoneNumber, Sex sex, Nation nationality,
            ResidenceStatus residenceStatus) {
        this.temporaryStayAbsentRequestId = -1;
        this.personId = personId;
        this.familyId = familyId;
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
        this.citizenIdentificationNumber = citizenIdentificationNumber;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.nationality = nationality;
        this.residenceStatus = residenceStatus;
    }

    @JsonIgnore
    public static TemporaryStayAbsentRequest convertFromMap (Map<String, Object> map)
    {
        TemporaryStayAbsentRequest t = new TemporaryStayAbsentRequest (
            (int)map.get("person_id"),
            (int)map.get("family_id"),
            (String)map.get("fullname"),
            Date.cast((String)map.get("date_of_birth")),
            (String)map.get("citizen_identification_number"),
            (String)map.get("phone_number"),
            Sex.matchByString((String)map.get("sex")).get(),
            Nation.matchByString ((String)map.get("nationality")).get(),
            ResidenceStatus.matchByString((String)map.get("residence_status")).get()
        );
        t.setId ((int)map.get("temporary_stay_absent_request_id"));
        return t;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return temporaryStayAbsentRequestId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        this.temporaryStayAbsentRequestId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return new String("temporary_stay_absent_request");
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (person_id,family_id,fullname,date_of_birth,citizen_identification_number,phone_number,sex,nationality,residence_status) values ('%d','%d','%s','%s','%s','%s','%s','%s','%s');", this.getSQLTableName(), personId, familyId, fullname, dateOfBirth, citizenIdentificationNumber, phoneNumber, sex, nationality, residenceStatus);
    }
    @Override
    @JsonIgnore
    public String getSQLUpdateCommand() {
        return String.format("UPDATE %s SET person_id='%d',family_id='%d',fullname='%s',date_of_birth='%s',citizen_identification_number='%s',phone_number='%s',sex='%s',nationality='%s',residence_status='%s' WHERE %s_id='%d';",
                this.getSQLTableName(),
                personId, familyId, fullname, dateOfBirth, citizenIdentificationNumber, phoneNumber, sex, nationality, residenceStatus,
                this.getSQLTableName(), getId());
    }

    public int getTemporaryStayAbsentRequestId() {
        return temporaryStayAbsentRequestId;
    }

    public void setTemporaryStayAbsentRequestId(int temporaryStayAbsentRequestId) {
        this.temporaryStayAbsentRequestId = temporaryStayAbsentRequestId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCitizenIdentificationNumber() {
        return citizenIdentificationNumber;
    }

    public void setCitizenIdentificationNumber(String citizenIdentificationNumber) {
        this.citizenIdentificationNumber = citizenIdentificationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Nation getNationality() {
        return nationality;
    }

    public void setNationality(Nation nationality) {
        this.nationality = nationality;
    }

    public ResidenceStatus getResidenceStatus() {
        return residenceStatus;
    }

    public void setResidenceStatus(ResidenceStatus residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

}
