package com.hust.ittnk68.cnpm.type;

import java.util.Arrays;
import java.util.Optional;

public enum ResidenceStatus {

    PRESENT("present"),
    ABSENT("absent");

    private String residenceStatus;

    ResidenceStatus(String residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

    public static Optional<ResidenceStatus> matchByString(String name) {
        return Arrays.stream(values()).filter(it -> it.toString().equals(name)).findAny();
    } 

    @Override
    public String toString() {
        return residenceStatus;
    }
}
