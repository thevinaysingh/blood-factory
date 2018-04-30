package com.majavrella.bloodinformer.modal;

/**
 * Created by Vinay on 2/26/2017.
 */

public class Donar {

    private static String selfRefKey, name, gender, ageGroup, bloodGroup, mobile, address, country, state, city, availability, authorization, userId;
    private static String isUser;

    public Donar() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters for isUser
    public String getIsUser() {
        return isUser;
    }
    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    //Getters and setters for selfRefKey
    public String getSelfRefKey() {
        return selfRefKey;
    }
    public void setSelfRefKey(String selfRefKey) {
        this.selfRefKey = selfRefKey;
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

    //Getters and setters for address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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

    //Getters and setters for availability
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    //Getters and setters for authorization
    public String getAuthorization() {
        return authorization;
    }
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    //Getters and setters for user id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Getters and setters for country
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
