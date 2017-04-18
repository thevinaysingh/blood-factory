package com.majavrella.bloodfactory.base;

import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONObject;

/**
 * Created by Vinay on 4/5/2017.
 */

public class UserProfileManager {

    private static UserProfileManager mInstance = null;
    private String mobile, password, usersDbRefKey, userListSelfRefKey, donar, patient, userId;
    private String address, ageGroup, bloodGroup, city, country, dob, emailId, gender, identity, name, profilePic, userListDbRefKey, usersSelfRefKey, state;

    private UserProfileManager(){
        address = RegisterConstants.defaultVarType;
        ageGroup = RegisterConstants.defaultVarType;
        bloodGroup = RegisterConstants.defaultVarType;
        city = RegisterConstants.defaultVarType;
        country = RegisterConstants.defaultVarType;
        dob = RegisterConstants.defaultVarType;
        emailId = RegisterConstants.defaultVarType;
        gender = RegisterConstants.defaultVarType;
        identity = RegisterConstants.defaultVarType;
        name = RegisterConstants.defaultVarType;
        profilePic = RegisterConstants.defaultVarType;
        userListDbRefKey = RegisterConstants.defaultVarType;
        usersSelfRefKey = RegisterConstants.defaultVarType;
        state = RegisterConstants.defaultVarType;

        donar= RegisterConstants.defaultVarType;
        mobile = RegisterConstants.defaultVarType;
        password = RegisterConstants.defaultVarType;
        patient = RegisterConstants.defaultVarType;
        usersDbRefKey = RegisterConstants.defaultVarType;
        userListSelfRefKey = RegisterConstants.defaultVarType;
        userId = RegisterConstants.defaultVarType;
    }

    // create new instance
    public static UserProfileManager getInstance(){
        if(mInstance == null)
        {
            mInstance = new UserProfileManager();
        }
        return mInstance;
    }

    public static void deleteInstance(){
        if(mInstance != null)
        {
            mInstance = null;
        }
    }

   /* setters for address, ageGroup, bloodGroup, city, country, dob, emailId,
    gender, identity, name, profilePic, userListDbRefKey, usersSelfRefKey, state; */

    public void setUserData(JSONObject jsonObject){
        try {
            address = jsonObject.getString("address");
            ageGroup = jsonObject.getString("ageGroup");
            bloodGroup = jsonObject.getString("bloodGroup");
            city = jsonObject.getString("city");
            country = jsonObject.getString("country");
            dob = jsonObject.getString("dob");
            emailId = jsonObject.getString("emailId");
            gender = jsonObject.getString("gender");
            identity = jsonObject.getString("identity");
            name = jsonObject.getString("name");
            profilePic = jsonObject.getString("profilePic");
            userListDbRefKey = jsonObject.getString("refKey");
            usersSelfRefKey = jsonObject.getString("selfRefKey");
            state = jsonObject.getString("state");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Getters for address
    public  String getAddress() {
        return address;
    }

    //Getters for ageGroup
    public String getAgeGroup() {
        return ageGroup;
    }

    //Getters for blood group
    public  String getBloodGroup() {
        return bloodGroup;
    }

    //Getters for city
    public  String getCity() {
        return city;
    }

    //Getters for country
    public String getCountry() {
        return country;
    }

    //Getters for dob
    public  String getDob() {
        return dob;
    }

    //Getters for email id
    public String getEmailId() {
        return emailId;
    }

    //Getters for gender
    public  String getGender() {
        return gender;
    }

    //Getters for identity
    public String getIdentity() {
        return identity;
    }

    //Getters for name
    public String getName() {
        return name;
    }

    //Getters for profile pic
    public  String getProfilePic() {
        return profilePic;
    }

    //Getters for userListDbRefKey
    public String getUserListDbRefKey() {
        return userListDbRefKey;
    }

    //Getters for usersSelfRefKey
    public String getUsersSelfRefKey() {
        return usersSelfRefKey;
    }

    //Getters for state
    public  String getState() {
        return state;
    }

    /*setters for mobile, password, usersDbRefKey, userListSelfRefKey, donar, patient, userId*/
    public void setUserListData(JSONObject jsonObject){
        try {
            donar = jsonObject.getString("donar");
            mobile = jsonObject.getString(Constants.kMobileString);
            password = jsonObject.getString(Constants.kPasswordString);
            patient = jsonObject.getString("patient");
            usersDbRefKey = jsonObject.getString(Constants.kRefKey);
            userListSelfRefKey = jsonObject.getString("selfRefKey");
            userId = jsonObject.getString("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getter for donar
    public String getDonar() {
        return donar;
    }

    // getter for mobile
    public String getMobile() {
        return mobile;
    }

    // getter for password
    public String getPassword() {
        return password;
    }

    // getter for patient
    public String getPatient() {
        return patient;
    }

    //getter for usersDbRefKey
    public String getUsersDbRefKey() {
        return usersDbRefKey;
    }

    // getter for  userListSelfRefKey
    public String getUserListSelfRefKey() {
        return userListSelfRefKey;
    }

    // getters for user id
    public String getUserId() {
        return userId;
    }
}
