package com.hust.ittnk68.cnpm.type;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Date {
    int year;
    int month;
    int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear () {
        return year;
    }
    public void setYear (int year) {
        this.year = year;
    }

    public int getMonth () {
        return month;
    }
    public void setMonth (int month) {
        this.month = month;
    }

    public int getDay () {
        return day;
    }
    public void setDay (int day) {
        this.day = day;
    }

    @JsonIgnore
    public static Date convertFromLocalDate (LocalDate localDate) {
        return new Date (localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    @Override
    @JsonIgnore
    public String toString() {
        return String.format("%d-%d-%d", year, month, day);
    }
}
