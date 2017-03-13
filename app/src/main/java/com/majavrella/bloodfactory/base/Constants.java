package com.majavrella.bloodfactory.base;

/**
 * Created by Administrator on 3/2/2017.
 */

public class Constants {

    public static final String kConsumerKey = "OcDNJGwlUg1XtfdGjmv4riYAC";
    public static final String kConsumerSecret = "KiCE5JBOsqkgOew9T9sCIfsFIs1w4A6MizjUtC4BTcU5raOiHT";

    public static final String nameRegex = "^[\\p{L}\\s]{1,}+$";
    public static final String emailRegex = "^[\\p{L}0-9!$'*+\\-_]+(\\.[\\p{L}0-9!$'*+\\-_]+)*@[\\p{L}0-9]+(\\-[\\p{L}0-9]+)*(\\.[\\p{L}0-9]+)*(\\.[\\p{L}]{2,})$";
    public static final String mobRegex = "^[0-9]{10}$";
    public static final String dateRegex = "^[0-3]{1}+[0-9]{1}/[0-1]{1}+[0-9]{1}/[0-9]{4}$";

    public static final String kFirstFragment = "First Page";
    public static final String kLoginFragment = "Login";
    public static final String kRegisterFragment = "Register";
    public static final String kHomeFragment = "Home";
    public static final String kAddMemberFragment = "Add Member";
    public static final String kDonateBloodFragment = "Donate Blood";
    public static final String kSearchBloodFragment = "Search Blood";
    public static final String kBloodRequestFragment = "Post Blood request";
    public static final String kPeopleInNeedFragment = "People in need";
    public static final String kChangePasswordFragment = "Change Password";
    public static final String kExtraSettingsFragment = "Extra Settings";
    public static final String kEditProfileFragment = "Edit Profile";
    public static final String kNotificationFragment = "Notification";
    public static final String kGuidanceFragment = "Guidance";
    public static final String kAboutUsFragment = "About us";
    public static final String kFAQFragment = "FAQs";

    public static final String nameErrorText = "Name is not valid!";
    public static final String emailErrorText = "Email is not valid!";
    public static final String mobErrorText = "Mobile no is not valid!";
    public static final String dateErrorText = "Please enter valid date!";
    public static final String genderErrorText = "Unknown identity!";
    public static final String ageErrorText = "Please, Select an age group!";
    public static final String bloodGroupErrorText = "Select a blood group";
    public static final String commonErrorText = "It is not valid";
    public static final String stateErrorText = "Please, Select a state";
    public static final String cityErrorText = "Please, select a city";
}
