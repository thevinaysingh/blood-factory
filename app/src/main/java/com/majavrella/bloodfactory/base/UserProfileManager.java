package com.majavrella.bloodfactory.base;

import com.majavrella.bloodfactory.register.RegisterConstants;

/**
 * Created by Administrator on 4/5/2017.
 */

public class UserProfileManager {

    private static String userListDbRefKey, usersDbRefKey, identity ="User", name= RegisterConstants.defaultVarType, country=RegisterConstants.defaultVarType;
    private static String emailId=RegisterConstants.defaultVarType, gender=RegisterConstants.defaultVarType, bloodGroup=RegisterConstants.defaultVarType;
    private static String availibility= RegisterConstants.defaultVarType,city=RegisterConstants.defaultVarType, state=RegisterConstants.defaultVarType;
    private static String profilePic = RegisterConstants.defaultVarType, dob=RegisterConstants.defaultVarType, address=RegisterConstants.defaultVarType;

    public static void setUserListDbRefKey(final String refKey) {
        userListDbRefKey = refKey;
    }
    public static void setUserDbRefKey(final String refKey) {
        usersDbRefKey = refKey;
    }

    public static String getUserListDbRefKey() {
        return userListDbRefKey;
    }
    public static String getUserDbRefKey() {
        return usersDbRefKey;
    }

    public static void resetUserListDbRefKey() {
        userListDbRefKey = "";
    }
    public static void resetUserDbRefKey() {
        usersDbRefKey = "";
    }

    //Getters and setters for identity
    public static String getIdentity() {
        return identity;
    }
    public static void setIdentity(String str) {
        identity = str;
    }

    //Getters and setters for name
    public static String getName() {
        return name;
    }
    public static void setName(String str) {
        name = str;
    }

    //Getters and setters for email id
    public static String getEmailId() {
        return emailId;
    }
    public static void setEmailId(String email) {emailId = email; }

    //Getters and setters for gender
    public static String getGender() {
        return gender;
    }
    public static void setGender(String gen) {
        gender = gen;
    }

    //Getters and setters for blood group
    public static String getBloodGroup() {
        return bloodGroup;
    }
    public static void setBloodGroup(String blood) {
        bloodGroup = blood;
    }

    //Getters and setters for dob
    public static String getDob() {
        return dob;
    }
    public static void setDob(String str) {
        dob = str;
    }

    //Getters and setters for address
    public static String getAddress() {
        return address;
    }
    public static void setAddress(String str) {
        address = str;
    }

    //Getters and setters for city
    public static String getCity() {
        return city;
    }
    public static void setCity(String citytext) {
        city = citytext;
    }

    //Getters and setters for state
    public static String getState() {
        return state;
    }
    public static void setState(String stateText) {
        state = stateText;
    }

    //Getters and setters for country
    public  static String getCountry() {
        return country;
    }
    public static void setCountry(String string) {
        country = string;
    }

    //Getters and setters for availibility
    public static String getAvailibility() {
        return availibility;
    }
    public static void setAvailibility(String string) {
        availibility = string;
    }

    //Getters and setters for profile pic
    public static String getProfilePic() {
        return profilePic;
    }
    public static void setProfilePic(String string) {
        profilePic = string;
    }
}
