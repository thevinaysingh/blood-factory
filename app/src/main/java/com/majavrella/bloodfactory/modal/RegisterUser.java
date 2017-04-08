package com.majavrella.bloodfactory.modal;

import com.majavrella.bloodfactory.register.RegisterConstants;

/**
 * Created by Administrator on 3/12/2017.
 */

public class RegisterUser {
    private static String refKey, userId, mobile, password, user = RegisterConstants.kTrue , donar = RegisterConstants.kFalse, patient = RegisterConstants.kFalse;

    public RegisterUser() {
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

    //Getters and setters for mobile
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //Getters and setters for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    //Getters and setters for user
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    //Getters and setters for donar
    public String getDonar() {
        return donar;
    }
    public void setDonar(String donar) {
        this.donar = donar;
    }

    //Getters and setters for patient
    public String getPatient() {
        return patient;
    }
    public void setPatient(String patient) {
        this.patient = patient;
    }

}
