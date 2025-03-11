package com.hust.ittnk68.cnpm.type;
                     
public enum Sex {
    MALE("Male"),
    FEMALE("Female");

    private String sex;

    Sex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return sex;
    }
}
