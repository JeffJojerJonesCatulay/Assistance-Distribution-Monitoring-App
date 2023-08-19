package com.example.assistancedistributionmonitoringapp;

public class CompleteList {
    private String Surname;
    private String FirstName;
    private String MiddleName;
    private String Type;
    private String NotedBy;

    public CompleteList() {
    }

    public CompleteList(String surname, String firstName, String middleName, String type, String notedBy) {
        Surname = surname;
        FirstName = firstName;
        MiddleName = middleName;
        Type = type;
        NotedBy = notedBy;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNotedBy() {
        return NotedBy;
    }

    public void setNotedBy(String notedBy) {
        NotedBy = notedBy;
    }
}
