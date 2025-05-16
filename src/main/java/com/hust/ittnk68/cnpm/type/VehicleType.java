package com.hust.ittnk68.cnpm.type;

import java.util.Arrays;
import java.util.Optional;

public enum VehicleType {
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

    public static Optional<VehicleType> matchByString(String name) {
        return Arrays.stream(values()).filter(it -> it.toString().equals(name)).findAny();
    }
}
