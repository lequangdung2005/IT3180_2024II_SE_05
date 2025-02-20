package com.hust.ittnk68.cnpm.model;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;

public class Vehicle extends GetSQLProperties {
    private int vehicleId;
    private int familyId;
    private String plateId;

    public Vehicle(int familyId, String plateId) {
        this.vehicleId = -1;
        this.familyId = familyId;
        this.plateId = plateId;
    }

    @Override
    public void setId(int id) {
        vehicleId = id;
    }
    @Override
    public String getSQLTableName() {
        return new String("vehicle");
    }
    @Override
    public String getSQLInsertCommand() {
        return String.format("INSERT INTO %s (family_id,plate_id) values ('%d','%s');", this.getSQLTableName(), familyId, plateId);
    }
}
