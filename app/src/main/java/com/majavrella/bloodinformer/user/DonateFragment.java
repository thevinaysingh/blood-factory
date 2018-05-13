package com.majavrella.bloodinformer.user;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserFragment;
import com.majavrella.bloodinformer.modal.Donar;
import com.majavrella.bloodinformer.register.RegisterConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends UserFragment {

    private static View mDonateFragment;
    private static String name, gender, age, bloodGroup, mob, address,country, state, city, availability, authorization;
    private SharedPreferences mSharedpreferences;
    private String ref_key;
    @Bind(R.id.donate_blood_page) LinearLayout mDonateBloodPage;
    @Bind(R.id.gender_error) TextView mGenderError;
    @Bind(R.id.gender_error_layout) LinearLayout mGenderErrorLayout;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.address_error) TextView mAddressError;
    @Bind(R.id.address_error_layout) LinearLayout mAddressErrorLayout;

    @Bind(R.id.donar_name) EditText mDonarName;
    @Bind(R.id.gender_status) RadioGroup mGenderStatus;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.donar_blood_group) Spinner mDonarBloodGroup;
    @Bind(R.id.donar_mob) EditText mDonarMob;
    @Bind(R.id.donar_address) EditText mDonarAddress;
    @Bind(R.id.donar_state) Spinner mDonarState;
    @Bind(R.id.donar_city) Spinner mDonarCity;
    @Bind(R.id.donar_status) RadioGroup mAvailabilityStatus;
    @Bind(R.id.donar_authorization) CheckBox mDonarAuthorization;
    @Bind(R.id.donate_button) Button mDonateButton;
    @Bind(R.id.edit_blood_page) LinearLayout mEditBloodPage;

    private static Donar editDonar;
    @Bind(R.id.donar_name_edit) EditText mEditDonarName;
    @Bind(R.id.donar_mob_edit) EditText mEditDonarMob;
    @Bind(R.id.donar_address_edit) EditText mEditDonarAddress;
    @Bind(R.id.donar_state_edit) Spinner mEditDonarState;
    @Bind(R.id.donar_city_edit) Spinner mEditDonarCity;
    @Bind(R.id.saved_state_city) TextView mEditStateCity;
    @Bind(R.id.blood_group_edit_box) Spinner mBlood_group_edit_box;
    @Bind(R.id.gender_edit_box) RadioGroup mGender_edit_box;
    @Bind(R.id.gender_edit_male) RadioButton mGender_edit_male;
    @Bind(R.id.gender_edit_female) RadioButton mGender_edit_female;
    @Bind(R.id.donar_blood_group_edit) TextView mEditDonarBloodGroup;
    @Bind(R.id.age_group_edit) RadioGroup mEditAgeGroup;
    @Bind(R.id.above18_edit) RadioButton mEditAbove18;
    @Bind(R.id.above35_edit) RadioButton mEditAbove35;
    @Bind(R.id.donar_status_edit) RadioGroup mEditAvailabilityStatus;
    @Bind(R.id.donarActive_edit) RadioButton mEditDonarActive;
    @Bind(R.id.donarInactive_edit) RadioButton mEditDonarInactive;
    @Bind(R.id.donar_authorization_edit) CheckBox mEditDonarAuthorization;
    @Bind(R.id.edit_button) Button mEditButton;

    @Bind(R.id.success_page) LinearLayout mSuccessPage;
    @Bind(R.id.goto_home) Button mGotoHome;

    public static DonateFragment newInstance() {
        return new DonateFragment();
    }

    public DonateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDonateFragment = inflater.inflate(R.layout.fragment_donate, container, false);
        ButterKnife.bind(this, mDonateFragment);

        mSharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        setStatusBarColor(Constants.colorStatusBarSecondary);
        mGenderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideKeyboard(getActivity());
                hideIt(mGenderErrorLayout); }
        });
        mDonarBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mBloodGrpErrorLayout);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });

        mEditDonarState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                setCities(mEditDonarCity, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });

        mDonarState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mAddressErrorLayout);
                }
                setCities(mDonarCity, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });
        mDonarCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mAddressErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });
        mDonateButton.setOnClickListener(mDonateButtonListener);
        mEditButton.setOnClickListener(mEditFragmentListener);
        mGotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent self = new Intent(getActivity(), UserActivity.class);
                self.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(self);
            }
        });
        return mDonateFragment;
    }

    View.OnClickListener mDonateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetModalData();
                setDataInStringFormat();
                if(dataValidation()){
                    progress.setMessage(RegisterConstants.waitProgress);
                    progress.show();
                    progress.setCancelable(false);
                    try {
                        checkIfUserHasDonatedAlready((userProfileManager.getUserListSelfRefKey()));
                    } catch (Exception e){
                        e.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(mActivity, "Operation failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "Input error", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNetworkError(mDonateFragment, RegisterConstants.networkErrorText);
            }
        }
    };

    private void resetModalData() {
        name = gender = age = bloodGroup = mob = address = state = city = country = availability = authorization = null;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mDonarName);
        if(mGenderStatus.getCheckedRadioButtonId()>=0){
            gender = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mGenderStatus.getCheckedRadioButtonId()));
        }
        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            age = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }
        bloodGroup = getStringDataFromSpinner(mDonarBloodGroup);
        mob = getStringDataFromEditText(mDonarMob);
        address = getStringDataFromEditText(mDonarAddress);
        state = getStringDataFromSpinner(mDonarState);
        city = getStringDataFromSpinner(mDonarCity);
        availability = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mAvailabilityStatus.getCheckedRadioButtonId()));
        authorization = mDonarAuthorization.isChecked()? "True" : "False";
        country = "India";
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(!isNameValid(name)){
            mDonarName.setError(Constants.nameErrorText);
            validation = false;
        }
        if(mGenderStatus.getCheckedRadioButtonId()<0){
            setErrorMsg(mGenderErrorLayout, mGenderError, Constants.genderErrorText);
            validation = false;
        }
        if(bloodGroup.equals("--Select blood group--")){
            setErrorMsg(mBloodGrpErrorLayout, mBloodGrpError, Constants.bloodGroupErrorText);
            validation = false;
        }
        if(mob.equals("")||!isPhoneValid(mob)){
            mDonarMob.setError(Constants.mobErrorText);
            validation = false;
        }
        if(address.equals("")){
            mDonarAddress.setError(Constants.commonErrorText);
            validation = false;
        }
        if(state.equals("--Select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Please select your State");
            validation = false;
        } else if(city.equals("--Select city--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Select your City");
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        return validation;
    }

    private void checkIfUserHasDonatedAlready(final String refKey) {
        String url = Constants.kBaseUrl+Constants.kUserListUrl+refKey+Constants.jsonTail;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        if (verifyDonation(json)){
                            progress.dismiss();
                            showEditPage();
                        } else {
                            setDataOnCloud();
                        }
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        progress.dismiss();
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        progress.dismiss();
                        break;
                    default : {
                        progress.dismiss();
                    }
                }
            }
        });
    }

    private void setDataOnCloud() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try{
                    DatabaseReference mDonarListDatabase = getRootReference().child(RegisterConstants.user_list_db);
                    mDonarListDatabase.child(userProfileManager.getUserListSelfRefKey()).child("donar").setValue(RegisterConstants.kTrue);
                    DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
                    String temp_key = mDonarsDatabase.push().getKey();
                    Donar donar = setDataInModal(new Donar());
                    donar.setSelfRefKey(temp_key);
                    donar.setIsUser(RegisterConstants.kTrue);
                    mDonarsDatabase.child(temp_key).setValue(donar);
                    DatabaseReference mUsersDatabase = getRootReference().child(RegisterConstants.user_Data_db);
                    mUsersDatabase.child(userProfileManager.getUsersSelfRefKey()).child("bloodGroup").setValue(bloodGroup);
                    mUsersDatabase.child(userProfileManager.getUsersSelfRefKey()).child("ageGroup").setValue(age);
                    mUsersDatabase.child(userProfileManager.getUsersSelfRefKey()).child("address").setValue(address);
                    userProfileManager.setDonar(RegisterConstants.kTrue);
                    resetAllField();
                    resetModalData();
                    progress.dismiss();
                    showSuccessMsgDonated();
                }catch (Exception e){
                    Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        }, 2000);
    }

    private Donar setDataInModal(Donar donar) {
        donar.setName(name);
        donar.setGender(gender);
        donar.setBloodGroup(bloodGroup);
        donar.setAgeGroup(age);
        donar.setMobile(mob);
        donar.setAddress(address);
        donar.setCountry(country);
        donar.setState(state);
        donar.setCity(city);
        donar.setAvailability(availability);
        donar.setAuthorization(authorization);
        donar.setUserId(getCurrentUserId());
        return donar;
    }

    private void resetAllField() {
        mDonarName.setText("");
        mDonarMob.setText("");
        mDonarAddress.setText("");
        setCities(mDonarCity, "--Select city--");
        resetState(mDonarState);
        resetBlood(mDonarBloodGroup);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        if(isNetworkAvailable()){
            if(userProfileManager.getDonar().equals(RegisterConstants.kTrue)){
                showEditPage();
            } else {
                isUserDonatedAlready(userProfileManager.getUserListSelfRefKey());
            }
        } else {
            showNetworkError(mDonateFragment, RegisterConstants.networkErrorText);
        }
        super.onResume();
    }

    private void showEditPage() {
        progress.setMessage(RegisterConstants.waitProgress);
        progress.show();
        progress.setCancelable(false);
        mDonateBloodPage.setVisibility(View.GONE);
        mEditBloodPage.setVisibility(View.VISIBLE);
        startEditing();
    }

    private void isUserDonatedAlready(final String refKey) {
        String url = Constants.kBaseUrl+Constants.kUserListUrl+refKey+Constants.jsonTail;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        if (verifyDonation(json)){
                            showEditPage();
                        } else {
                            setUserData();
                        }
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        break;
                    default : {
                    }
                }
                progress.dismiss();
            }
        });
    }

    private void setUserData() {
        mDonarName.setText(userProfileManager.getName());
        mDonarMob.setText(userProfileManager.getMobile());
    }

    private boolean verifyDonation(JSONObject json) {
        try {
            if(json.getString("donar").equals(RegisterConstants.kTrue)){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startEditing() {
        String url = Constants.kBaseUrl+Constants.kDonars;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        try {
                            JSONObject josnObject = new JSONObject(fetchDonarsData(json));
                            editDonar = setDonar(josnObject, new Donar());
                            setDonarsDataToEdit();
                        } catch (JSONException e) {
                            progress.dismiss();
                            e.printStackTrace();
                        }
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        progress.dismiss();
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        progress.dismiss();
                        break;
                    default : {
                        progress.dismiss();
                    }
                }
            }
        });
    }

    private void setDonarsDataToEdit() {
        mEditDonarName.setText(editDonar.getName());
        mEditDonarMob.setText(editDonar.getMobile());
        mEditDonarAddress.setText(editDonar.getAddress());
        mEditDonarBloodGroup.setText(editDonar.getBloodGroup());
        mEditStateCity.setText(editDonar.getCity()+", "+editDonar.getState());
        if(editDonar.getAgeGroup().equals("18+")){
            mEditAbove18.setChecked(true);
        } else {
            mEditAbove35.setChecked(true);
        }
        if(editDonar.getGender().equals("Male")){
            mGender_edit_female.setChecked(false);
            mGender_edit_male.setChecked(true);
        } else {
            mGender_edit_female.setChecked(true);
            mGender_edit_male.setChecked(false);
        }
        if(editDonar.getAvailability().equals("Unavailable")){
            mEditDonarInactive.setChecked(true);
        } else {
            mEditDonarActive.setChecked(true);
        }
        progress.dismiss();
    }
    
    private String fetchDonarsData(JSONObject json) {
        String jsonData = null;
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(userProfileManager.getUserId())
                        &&json.getJSONObject(key).get("isUser").toString().equals(RegisterConstants.kTrue)){
                    jsonData = json.getJSONObject(key).toString();
                    return jsonData;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonData;
    }

    View.OnClickListener mEditFragmentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetModalData();
                setEditDataInStringFormat();
                if(dataEditValidation()){
                    editDataOnCloud();
                } else {
                    Toast.makeText(mActivity, "Input error", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNetworkError(mDonateFragment, RegisterConstants.networkErrorText);
            }
        }
    };

    private void setEditDataInStringFormat() {
        name = getStringDataFromEditText(mEditDonarName);
        if(mEditAgeGroup.getCheckedRadioButtonId()>=0){
            age = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mEditAgeGroup.getCheckedRadioButtonId()));
        }

        if(mGender_edit_box.getCheckedRadioButtonId()>=0){
            gender = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mGender_edit_box.getCheckedRadioButtonId()));
        }

        bloodGroup = getStringDataFromSpinner(mBlood_group_edit_box);
        mob = getStringDataFromEditText(mEditDonarMob);
        address = getStringDataFromEditText(mEditDonarAddress);
        state = getStringDataFromSpinner(mEditDonarState);
        city = getStringDataFromSpinner(mEditDonarCity);
        availability = getStringDataFromRadioButton((RadioButton) mDonateFragment.findViewById(mEditAvailabilityStatus.getCheckedRadioButtonId()));
        authorization = mEditDonarAuthorization.isChecked()? RegisterConstants.kTrue : RegisterConstants.kFalse;
    }

    private boolean dataEditValidation() {
        boolean validation = true;
        if(!isNameValid(name)){
            mEditDonarName.setError(Constants.nameErrorText);
            validation = false;
        }

        if(mob.equals("")||!isPhoneValid(mob)){
            mEditDonarMob.setError(Constants.mobErrorText);
            validation = false;
        }
        if(address.equals("")){
            mEditDonarAddress.setError(Constants.commonErrorText);
            validation = false;
        }
        if(bloodGroup.equals("--Select blood group--")){
            bloodGroup = editDonar.getBloodGroup();
        }

        if(state.equals("--Select state--")){
            state = editDonar.getState();
            city = editDonar.getCity();
        } else {
            if(city.equals("--Select city--")){
                showNetworkError(mDonateFragment, "Select city");
                validation = false;
            }
        }


        return validation;
    }

    private void editDataOnCloud() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you really want to edit, if yes click ok and start processing...")
                .setTitle("Update blood detail")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkAvailable()){
                            progress.setMessage(RegisterConstants.waitProgress);
                            progress.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    try{
                                        editDonar.setName(name);
                                        editDonar.setMobile(mob);
                                        editDonar.setAddress(address);
                                        editDonar.setCity(city);
                                        editDonar.setGender(gender);
                                        editDonar.setBloodGroup(bloodGroup);
                                        editDonar.setState(state);
                                        editDonar.setAgeGroup(age);
                                        editDonar.setAvailability(availability);
                                        editDonar.setAuthorization(authorization);
                                        DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
                                        mDonarsDatabase.child(editDonar.getSelfRefKey()).setValue(editDonar);
                                        Toast.makeText(mActivity, "Editing done", Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                        showSuccessMsg();
                                    }catch (Exception e){
                                        Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        progress.dismiss();
                                    }
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(mActivity, "No network available", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSuccessMsg() {
        mEditBloodPage.setVisibility(View.GONE);
        mSuccessPage.setVisibility(View.VISIBLE);
    }

    private void showSuccessMsgDonated() {
        getFragmentManager().popBackStackImmediate();
        Toast.makeText(mActivity, "You have created your blood donation profile successfully!", Toast.LENGTH_SHORT).show();
        //mDonateBloodPage.setVisibility(View.GONE);
        //mSuccessPage.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getTitle() {
        return "Donate Blood";
    }
}
