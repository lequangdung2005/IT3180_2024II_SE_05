package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateVehicleModel {
    
    private StringProperty vehicleType;
    private StringProperty familyId;
    private StringProperty plateId;

    public StringProperty getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(StringProperty vehicleType) {
        this.vehicleType = vehicleType;
    }

    public StringProperty getFamilyId() {
        return familyId;
    }

    public void setFamilyId(StringProperty familyId) {
        this.familyId = familyId;
    }

    public StringProperty getPlateId() {
        return plateId;
    }

    public void setPlateId(StringProperty plateId) {
        this.plateId = plateId;
    }

    public CreateVehicleModel() {
        vehicleType = new SimpleStringProperty(); 
        familyId = new SimpleStringProperty();
        plateId = new SimpleStringProperty();
    }

}
