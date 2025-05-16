package com.hust.ittnk68.cnpm.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;

public class FindVehicleModel {
    
    private StringProperty vehicleType;
    private StringProperty familyId;
    private StringProperty plateId;

    private TableView<Vehicle> tableView;

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

    public FindVehicleModel() {
        vehicleType = new SimpleStringProperty();
        familyId = new SimpleStringProperty();
        plateId = new SimpleStringProperty();
    }

    public void setTableView (TableView<Vehicle> tableView) {
        this.tableView = tableView;
    }
    public TableView<Vehicle> getTableView() {
        return tableView;
    }

}
