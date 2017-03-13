package com.majavrella.bloodfactory.modal;

/**
 * Created by Administrator on 3/12/2017.
 */

public class RegisterUser {
    private static String name, mobile, password;

    public RegisterUser() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters for mobile
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //Getters and setters for name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Getters and setters for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
