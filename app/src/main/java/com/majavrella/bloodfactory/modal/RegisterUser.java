package com.majavrella.bloodfactory.modal;

/**
 * Created by Administrator on 3/12/2017.
 */

public class RegisterUser {
    private static String refKey, userId, name, mobile, password;

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

}
