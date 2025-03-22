package com.hust.ittnk68.cnpm.type;

import java.util.Map;
import java.util.HashMap;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ExpenseType {
    SERVICE("service"),
    ELECTRIC("electric"),
    WATER("water"),
    VEHICLE_PARK("parking"),
    DONATION("donation");

    private String expenseType;
    ExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    @Override
    public String toString() {
        return expenseType;
    }

    @JsonIgnore
    static final Map<ExpenseType, Ikon> expenseTypeToIcon;
    static {
        expenseTypeToIcon = new HashMap<> ();
        expenseTypeToIcon.put (ExpenseType.SERVICE, (Material2AL.CLEANING_SERVICES));
        expenseTypeToIcon.put (ExpenseType.ELECTRIC, (Material2OutlinedMZ.OFFLINE_BOLT));
        expenseTypeToIcon.put (ExpenseType.VEHICLE_PARK, (Material2AL.DIRECTIONS_CAR));
        expenseTypeToIcon.put (ExpenseType.WATER, (Material2MZ.WATER_DAMAGE));
        expenseTypeToIcon.put (ExpenseType.DONATION, (Material2AL.FAVORITE));
    }
    @JsonIgnore
    static public Ikon getIkonCode (ExpenseType e) {
        return expenseTypeToIcon.get (e);
    }
}
