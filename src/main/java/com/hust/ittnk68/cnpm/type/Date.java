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
    public LocalDate convertToLocalDate () {
        return LocalDate.of (year, month, day);
    }

    @JsonIgnore
    public Date plusDays (long daysToAdd) {
        LocalDate localDate = this.convertToLocalDate().plusDays (daysToAdd);
        return Date.convertFromLocalDate (localDate); 
    }

    @JsonIgnore
    public static Date cast (String value) {
        String[] arr = value.split ("-");
        return new Date (Integer.parseInt (arr[0]),
                        Integer.parseInt (arr[1]),
                        Integer.parseInt (arr[2]));
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
