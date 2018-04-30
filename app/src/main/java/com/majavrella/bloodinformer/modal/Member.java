package com.majavrella.bloodinformer.modal;

/**
 * Created by Administrator on 2/26/2017.
 */

public class Member {

    private String name;
    private String gender;
    private String ageGroup;
    private String bloodGroup;
    private String mobile;
    private String state;
    private String city;
    private String availability;

    public Member() {
      /*Blank default constructor essential for Firebase*/
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

    //Getters and setters for availability
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
