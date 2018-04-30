package com.majavrella.bloodinformer.modal;

import com.majavrella.bloodinformer.register.RegisterConstants;

/**
 * Created by Vinay on 2/26/2017.
 */

public class UserData {
    private static String userId= RegisterConstants.defaultVarType;
    private static String refKey= RegisterConstants.defaultVarType;
    private static String identity= "User";
    private static String name= RegisterConstants.defaultVarType;
    private static String emailId= RegisterConstants.defaultVarType;
    private static String gender= RegisterConstants.defaultVarType;
    private static String bloodGroup= RegisterConstants.defaultVarType;
    private static String ageGroup= RegisterConstants.defaultVarType;
    private static String dob= RegisterConstants.defaultVarType;
    private static String address= RegisterConstants.defaultVarType;
    private static String city= RegisterConstants.defaultVarType;
    private static String state= RegisterConstants.defaultVarType;
    private static String country= "India";
    private static String profilePic = RegisterConstants.defaultVarType;
    private static String selfRefKey;

    public UserData() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters for selfRefKey
    public String getSelfRefKey() {
        return selfRefKey;
    }
    public void setSelfRefKey(String selfRefKey) {
        this.selfRefKey = selfRefKey;
    }

    //Getters and setters for user Id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Getters and setters for ref key
    public String getRefKey() {
        return refKey;
    }
    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    //Getters and setters for identity
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String mobile) {
        this.identity = identity;
    }

    //Getters and setters for name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Getters and setters for email id
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {this.emailId = emailId; }

    //Getters and setters for gender
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    //Getters and setters for blood group
    public String getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    //Getters and setters for age group
    public String getAgeGroup() {
        return ageGroup;
    }
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    //Getters and setters for dob
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    //Getters and setters for address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    //Getters and setters for city
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    //Getters and setters for state
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    //Getters and setters for country
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    //Getters and setters for profile pic
    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
