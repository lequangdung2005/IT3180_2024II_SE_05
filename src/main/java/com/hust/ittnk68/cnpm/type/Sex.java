package com.hust.ittnk68.cnpm.type;

import java.util.Arrays;
import java.util.Optional;
                     
public enum Sex {
    MALE("Male"),
    FEMALE("Female");

    private String sex;

    Sex(String sex) {
        this.sex = sex;
    }

    public static Optional<Sex> matchByString(String name) {
        return Arrays.stream(values()).filter(it -> it.toString().equals(name)).findAny();
    } 

    @Override
    public String toString() {
        return sex;
    }
}
