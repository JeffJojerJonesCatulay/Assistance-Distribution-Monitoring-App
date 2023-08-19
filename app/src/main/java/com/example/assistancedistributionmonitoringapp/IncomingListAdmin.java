package com.example.assistancedistributionmonitoringapp;

public class IncomingListAdmin {
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
    private String DateSchedule;
    private String Time;
    private String Type;
    private String Specify;
    private String Sponsor;
    private String PostedBy;
    private String DatePosted;

    public IncomingListAdmin() {
    }

    public IncomingListAdmin(String surname, String firstName, String middleName, String age, String address, String gender, String civilStatus, String contact, String religion, String occupation, String dateSchedule, String time, String type, String specify, String sponsor, String postedBy, String datePosted) {
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
        DateSchedule = dateSchedule;
        Time = time;
        Type = type;
        Specify = specify;
        Sponsor = sponsor;
        PostedBy = postedBy;
        DatePosted = datePosted;
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

    public String getDateSchedule() {
        return DateSchedule;
    }

    public void setDateSchedule(String dateSchedule) {
        DateSchedule = dateSchedule;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSpecify() {
        return Specify;
    }

    public void setSpecify(String specify) {
        Specify = specify;
    }

    public String getSponsor() {
        return Sponsor;
    }

    public void setSponsor(String sponsor) {
        Sponsor = sponsor;
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String postedBy) {
        PostedBy = postedBy;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }
}
