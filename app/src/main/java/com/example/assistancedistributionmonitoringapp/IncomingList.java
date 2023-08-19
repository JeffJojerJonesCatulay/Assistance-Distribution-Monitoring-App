package com.example.assistancedistributionmonitoringapp;

public class IncomingList {

    private String Surname;
    private String FirstName;
    private String MiddleName;
    private String DateSchedule;
    private String Time;
    private String Type;
    private String Specify;
    private String Sponsor;
    private String PostedBy;
    private String DatePosted;

    public IncomingList() {
    }

    public IncomingList(String surname, String firstName, String middleName, String dateSchedule, String time, String type, String specify, String sponsor, String postedBy, String datePosted) {
        Surname = surname;
        FirstName = firstName;
        MiddleName = middleName;
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
