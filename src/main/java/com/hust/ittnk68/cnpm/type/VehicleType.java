package com.hust.ittnk68.cnpm.type;

public enum VehicleType {
    TRUCK("truck"),
    CAR("car"),
    BICYCLE("bicycle"),
    MOTORCYCLE("motorcycle"),
    ELECTRIC_BICYCLE("electric_bicycle");

    private String vehicleType;
    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return vehicleType;
    }
}
