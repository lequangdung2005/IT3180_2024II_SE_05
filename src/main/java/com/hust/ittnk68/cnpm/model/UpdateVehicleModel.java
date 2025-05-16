package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class UpdateVehicleModel {
    
    private StringProperty vehicleId; 
    private StringProperty vehicleType;
    private StringProperty familyId;
    private StringProperty plateId;

    private Stage updateVehicleWindow;

    public UpdateVehicleModel() {
        vehicleId = new SimpleStringProperty();
        vehicleType = new SimpleStringProperty(); 
        familyId = new SimpleStringProperty();
        plateId = new SimpleStringProperty();
    }

    public StringProperty getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(StringProperty vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Stage getUpdateVehicleWindow() {
        return updateVehicleWindow;
    }

    public void setUpdateVehicleWindow(Stage updateVehicleWindow) {
        this.updateVehicleWindow = updateVehicleWindow;
    }

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

}
