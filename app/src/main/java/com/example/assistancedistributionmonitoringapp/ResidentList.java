package com.example.assistancedistributionmonitoringapp;

public class ResidentList {
    private String Surname;
    private String FirstName;
    private String MiddleName;

    public ResidentList() {
    }

    public ResidentList(String surname, String firstName, String middleName) {
        Surname = surname;
        FirstName = firstName;
        MiddleName = middleName;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }
}
