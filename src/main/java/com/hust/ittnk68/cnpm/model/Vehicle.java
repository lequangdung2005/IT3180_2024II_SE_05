package com.hust.ittnk68.cnpm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.type.VehicleType;

public class Vehicle extends GetSQLProperties {
    private int vehicleId;
    private VehicleType vehicleType;
    private int familyId;
    private String plateId;

    public Vehicle() {}
    public Vehicle(int familyId, String plateId) {
        this.vehicleId = -1;
        this.familyId = familyId;
        this.plateId = plateId;
    }

    @Override
    @JsonIgnore
    public int getId() {
        return vehicleId;
    }
    @Override
    @JsonIgnore
    public void setId(int id) {
        vehicleId = id;
    }
    @Override
    @JsonIgnore
    public String getSQLTableName() {
        return new String("vehicle");
    }
    @Override
    @JsonIgnore
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (vehicle_type,family_id,plate_id) values ('%s','%d','%s');", this.getSQLTableName(), vehicleType, familyId, plateId);
    }
    @Override
    @JsonIgnore
    public String getSQLUpdateCommand() {
        return String.format("UPDATE %s SET vehicle_type='%s',family_id='%d',plate_id='%s' WHERE %s_id=%d;",
                this.getSQLTableName(),
                vehicleType, familyId, plateId,
                this.getSQLTableName(), getId());
    }
}
