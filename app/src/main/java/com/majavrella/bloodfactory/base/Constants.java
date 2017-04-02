package com.majavrella.bloodfactory.base;

/**
 * Created by Administrator on 3/2/2017.
 */

public class Constants {

    public static final String kConsumerKey = "OcDNJGwlUg1XtfdGjmv4riYAC";
    public static final String kConsumerSecret = "KiCE5JBOsqkgOew9T9sCIfsFIs1w4A6MizjUtC4BTcU5raOiHT";
    public static final String kBaseUrl = "https://blood-factory.firebaseio.com/";
    public static final String kUserList = "user_list.json";
    public static final String kUsersData = "users.json";
    public static final String kMobileString = "mobile";
    public static final String kPasswordString = "password";
    public static final String kRefKey = "refKey";
    public static final String kUserId = "userId";

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
    public static final String kBloodRequestFragment = "Blood request";
    public static final String kPeopleInNeedFragment = "People in need";
    public static final String kChangePasswordFragment = "Change Password";
    public static final String kExtraSettingsFragment = "Settings";
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
    public static final String colorGrey = "#848181";
    public static final String colorLogin = "#730015";
    public static final String colorUserHome = "#375270";
    public static final String colorStatusBarDark = "#6b6b6b";
    public static final String colorStatusBar = "#35586c";
    public static final String colorStatusBarSecondary = "#00796a";
    public static final String changeMobiletitle = "Change Mobile No";
    public static final String changeMobileMsg = "Your mobile number is treated as user id for blood factory application."
    + " This version of app does not allow to change your mobile number. We are sorry to keep it messy!";
}
