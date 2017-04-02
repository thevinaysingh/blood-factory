package com.majavrella.bloodfactory.modal;

import android.graphics.Bitmap;
import android.media.Image;

import com.majavrella.bloodfactory.R;

/**
 * Created by Administrator on 2/26/2017.
 */

public class UserData {
    private static String userId= "null", refKey="null", identity ="null", name="null", emailId="null", gender="null", bloodGroup="null", ageGroup="null", dob="null", address="null", city="null", state="null", country="null";
    private static String availibility="null", authorizedToApp="null", occupation="null", dor="null", tor="null", currentLoc="null", lastLoc="null";
    private static String profilePic = "null";

    public UserData() {
      /*Blank default constructor essential for Firebase*/
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

    //Getters and setters for availibility
    public String getAvailibility() {
        return availibility;
    }
    public void setAvailibility(String availibility) {
        this.availibility = availibility;
    }

    //Getters and setters for authorizedToApp
    public String getAuthorizedToApp() {
        return authorizedToApp;
    }
    public void setAuthorizedToApp(String authorizedToApp) {
        this.authorizedToApp = authorizedToApp;
    }

    //Getters and setters for profile pic
    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    //Getters and setters for occupation
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    //Getters and setters for date of registration
    public String getDor() {
        return dor;
    }
    public void setDor(String dor) {
        this.dor = dor;
    }

    //Getters and setters for time of registration
    public String getTor() {
        return tor;
    }
    public void setTor(String tor) {
        this.tor = tor;
    }

    //Getters and setters for current location
    public String getCurrentLoc() {
        return currentLoc;
    }
    public void setCurrentLoc(String currentLoc) {
        this.currentLoc = currentLoc;
    }

    //Getters and setters for last location
    public String getLastLoc() {
        return lastLoc;
    }
    public void setLastLoc(String lastLoc) {
        this.lastLoc = lastLoc;
    }
}
