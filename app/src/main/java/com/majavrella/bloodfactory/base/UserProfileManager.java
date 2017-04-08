package com.majavrella.bloodfactory.base;

import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONObject;

/**
 * Created by Vinay on 4/5/2017.
 */

public class UserProfileManager {

    private static UserProfileManager mInstance = null;
    
    private String mobile, password, usersDbRefKey;
    private String userId;
    private String userListDbRefKey,currentLoc,lastLoc, dor, occupation, tor, identity, ageGroup, authorizedToApp ,name, country, emailId, gender, bloodGroup, availibility, city, state, profilePic, dob, address;

    private UserProfileManager(){
        authorizedToApp = RegisterConstants.defaultVarType;
        ageGroup = RegisterConstants.defaultVarType;
        currentLoc = RegisterConstants.defaultVarType;
        lastLoc = RegisterConstants.defaultVarType;
        dor = RegisterConstants.defaultVarType;
        tor = RegisterConstants.defaultVarType;
        occupation = RegisterConstants.defaultVarType;
        mobile = RegisterConstants.defaultVarType;
        password = RegisterConstants.defaultVarType;
        name = RegisterConstants.defaultVarType;
        userId = RegisterConstants.defaultVarType;
        userListDbRefKey = RegisterConstants.defaultVarType;
        usersDbRefKey = RegisterConstants.defaultVarType;
        identity = RegisterConstants.defaultVarType;
        country = RegisterConstants.defaultVarType;
        emailId = RegisterConstants.defaultVarType;
        gender = RegisterConstants.defaultVarType;
        bloodGroup = RegisterConstants.defaultVarType;
        availibility = RegisterConstants.defaultVarType;
        city = RegisterConstants.defaultVarType;
        state = RegisterConstants.defaultVarType;
        profilePic = RegisterConstants.defaultVarType;
        dob = RegisterConstants.defaultVarType;
        address = RegisterConstants.defaultVarType;
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

    // set user id
    public void setUserId(final String userId) {
        this.userId = userId;
    }

    // getters for ref key , user id password and mobile
    public String getUserListDbRefKey() {
        return userListDbRefKey;
    }
    public String getUsersDbRefKey() {
        return usersDbRefKey;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public String getMobile() {
        return mobile;
    }

    //Getters for authorizedToApp
    public String getAuthorizedToApp() {
        return authorizedToApp;
    }

    //Getters for ageGroup
    public String getAgeGroup() {
        return ageGroup;
    }

    //Getters for tor
    public String getTor() {
        return tor;
    }

    //Getters for occupation
    public String getOccupation() {
        return occupation;
    }

    //Getters for dor
    public String getDor() {
        return dor;
    }

    //Getters for lastLoc
    public String getLastLoc() {
        return lastLoc;
    }

    //Getters for currentLoc
    public String getCurrentLoc() {
        return currentLoc;
    }

    //Getters for identity
    public String getIdentity() {
        return identity;
    }

    //Getters for name
    public String getName() {
        return name;
    }

    //Getters for email id
    public String getEmailId() {
        return emailId;
    }

    //Getters for gender
    public  String getGender() {
        return gender;
    }

    //Getters for blood group
    public  String getBloodGroup() {
        return bloodGroup;
    }

    //Getters for dob
    public  String getDob() {
        return dob;
    }

    //Getters for address
    public  String getAddress() {
        return address;
    }

    //Getters for city
    public  String getCity() {
        return city;
    }

    //Getters for state
    public  String getState() {
        return state;
    }

    //Getters for country
    public String getCountry() {
        return country;
    }

    //Getters for availibility
    public  String getAvailibility() {
        return availibility;
    }

    //Getters for profile pic
    public  String getProfilePic() {
        return profilePic;
    }

    public void setUserListData(JSONObject jsonObject){
        try {
            mobile = jsonObject.getString(Constants.kMobileString);
            password = jsonObject.getString(Constants.kPasswordString);
            usersDbRefKey = jsonObject.getString(Constants.kRefKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserData(JSONObject jsonObject){
        try {
            address = jsonObject.getString(Constants.kMobileString);
            ageGroup = jsonObject.getString("ageGroup");
            authorizedToApp = jsonObject.getString("authorizedToApp");
            availibility = jsonObject.getString("availibility");
            bloodGroup = jsonObject.getString("bloodGroup");
            city = jsonObject.getString("city");
            country = jsonObject.getString("country");
            currentLoc = jsonObject.getString("currentLoc");
            dob = jsonObject.getString("dob");
            dor = jsonObject.getString("dor");
            emailId = jsonObject.getString(emailId);
            gender = jsonObject.getString(gender);
            identity = jsonObject.getString("identity");
            lastLoc = jsonObject.getString("lastLoc");
            name = jsonObject.getString("name");
            occupation = jsonObject.getString("occupation");
            profilePic = jsonObject.getString("profilePic");
            userListDbRefKey = jsonObject.getString("refKey");
            state = jsonObject.getString("state");
            tor = jsonObject.getString("tor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
