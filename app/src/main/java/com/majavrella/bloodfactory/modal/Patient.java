package com.majavrella.bloodfactory.modal;

/**
 * Created by Administrator on 2/26/2017.
 */

public class Patient {
    private String name;
    private String userId;
    private String gender;
    private String ageGroup;
    private String bloodGroup;
    private String mobile;
    private String state;
    private String city;
    private String date;
    private String purpose;
    private String selfRefKey;
    private String helpingUsers="";
    private String status = "In progress";

    public Patient() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters for helping users
    public String getHelpingUsers() {
        return helpingUsers;
    }
    public void setHelpingUsers(String helpingUsers) {
        this.helpingUsers = helpingUsers;
    }

    //Getters and setters for status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    //Getters and setters for user id
    public String getSelfRefKey() {
        return selfRefKey;
    }
    public void setSelfRefKey(String selfRefKey) {
        this.selfRefKey = selfRefKey;
    }

    //Getters and setters for user id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Getters and setters for name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Getters and setters for gender
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    //Getters and setters for age group
    public String getAgeGroup() {
        return ageGroup;
    }
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    //Getters and setters for blood group
    public String getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    //Getters and setters for mobile
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //Getters and setters for state
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    //Getters and setters for city
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    //Getters and setters for last date of need
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    //Getters and setters for purpose
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
