package com.hust.ittnk68.cnpm.type;

public enum ResidenceStatus {

    PRESENT("present"),
    ABSENT("absent");

    private String residenceStatus;

    ResidenceStatus(String residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

    @Override
    public String toString() {
        return residenceStatus;
    }
}
