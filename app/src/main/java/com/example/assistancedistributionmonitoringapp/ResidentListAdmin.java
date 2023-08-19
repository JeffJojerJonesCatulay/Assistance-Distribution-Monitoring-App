package com.example.assistancedistributionmonitoringapp;

public class ResidentListAdmin {
    private String Surname;
    private String FirstName;
    private String MiddleName;
    private String Age;
    private String Address;
    private String Gender;
    private String CivilStatus;
    private String Contact;
    private String Religion;
    private String Occupation;

    public ResidentListAdmin() {
    }

    public ResidentListAdmin(String surname, String firstName, String middleName, String age, String address, String gender, String civilStatus, String contact, String religion, String occupation) {
        Surname = surname;
        FirstName = firstName;
        MiddleName = middleName;
        Age = age;
        Address = address;
        Gender = gender;
        CivilStatus = civilStatus;
        Contact = contact;
        Religion = religion;
        Occupation = occupation;
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

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getCivilStatus() {
        return CivilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        CivilStatus = civilStatus;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }
}
