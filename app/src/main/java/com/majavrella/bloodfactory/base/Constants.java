package com.majavrella.bloodfactory.base;

/**
 * Created by Administrator on 3/2/2017.
 */

public class Constants {
    public static final String nameRegex = "^[\\p{L}\\s]{1,}+$";
    public static final String emailRegex = "^[\\p{L}0-9!$'*+\\-_]+(\\.[\\p{L}0-9!$'*+\\-_]+)*@[\\p{L}0-9]+(\\-[\\p{L}0-9]+)*(\\.[\\p{L}0-9]+)*(\\.[\\p{L}]{2,})$";
    public static final String mobRegex = "^[0-9]{10}$";
    public static final String dateRegex = "^[0-3]{1}+[0-9]{1}/[0-1]{1}+[0-9]{1}/[0-9]{4}$";
    public static final String kAddMemberFragment = "Add Member";
    public static final String nameErrorText = "Name is not valid!";
    public static final String emailErrorText = "Email is not valid!";
    public static final String mobErrorText = "Mobile no is not valid!";
    public static final String dateErrorText = "Please enter valid date!";
    public static final String genderErrorText = "Unknown identity!";
    public static final String ageErrorText = "Please, Select an age group!";
    public static final String bloodGroupErrorText = "Select a blood group";
}
