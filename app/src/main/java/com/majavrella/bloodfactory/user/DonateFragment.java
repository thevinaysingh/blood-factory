package com.majavrella.bloodfactory.user;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.register.RegisterConstants;

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
    private boolean isUserDonated = false;

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
        mDonarState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        return mDonateFragment;
    }

    View.OnClickListener mDonateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            resetModalData();
            setDataInStringFormat();
            boolean isAllFieldsValid = dataValidation();
            if(isAllFieldsValid){
                if(isNetworkAvailable()) {
                    progress.setMessage("Saving donar...");
                    progress.show();
                    try {
                        //checkIfUserHasDonatedAlready();
                        setDataOnCloud();
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Operation failed", Toast.LENGTH_SHORT).show();
                    } finally {
                        progress.dismiss();
                    }
                } else {
                    showSnackbar(mDonateFragment, RegisterConstants.networkErrorText);
                }

            }else {
                Toast.makeText(mActivity, "Fill all fields", Toast.LENGTH_SHORT).show();
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
        if(state.equals("--select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Please select your State");
            validation = false;
        } else if(city.equals("--select city--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Select your City");
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        return validation;
    }

    private void setDataOnCloud() {
        DatabaseReference mDonarListDatabase = getRootReference().child(RegisterConstants.user_list_db);
        mDonarListDatabase.child("-KhYuMltgAeJ7t0GK_q3").child("donar").setValue(RegisterConstants.kTrue);
        Toast.makeText(mActivity, "Donar value updated", Toast.LENGTH_SHORT).show();
        DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
        String temp_key = mDonarsDatabase.push().getKey();
        Donar donar = setDataInModal(new Donar());
        mDonarsDatabase.child(temp_key).setValue(donar);
    }

    private void updateUserListDatabase() {

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

    private void checkIfUserHasDonatedAlready() {
        if(!ref_key.equals(RegisterConstants.defaultSharedPrefsValue)){
            isUserDonatedAlready(ref_key);
        } else {
            String url = Constants.kBaseUrl+Constants.kUserList;
            APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                @Override
                public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        if (verifyUserDonation(json)){
                            showSnackbar(mDonateFragment, "Already donated");
                        } else {
                            setDataOnCloud();
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
    }

    private boolean verifyUserDonation(JSONObject json) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
                    return verifyDonation(json.getJSONObject(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean verifyDonation(JSONObject json) {
        try {
            if(json.getString("donar").equals(RegisterConstants.kTrue)){
                Log.d("----------", "verifyDonation: "+json.getString("donar"));
               return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onResume() {
        ref_key = mSharedpreferences.getString(RegisterConstants.userListRefKey,RegisterConstants.defaultSharedPrefsValue);
        hideKeyboard(getActivity());
        if(!ref_key.equals(RegisterConstants.defaultSharedPrefsValue)){
            isUserDonatedAlready(ref_key);
        }

        super.onResume();
    }

    private void isUserDonatedAlready(final String refKey) {
        String url = Constants.kBaseUrl+Constants.kUserListUrl+refKey+Constants.jsonTail;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        if (verifyDonation(json)){
                            isUserDonated = true;
                            showSnackbar(mDonateFragment, "Already donated");
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

    public void showSnackbar(View view, String text) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDonateButton.setClickable(false);
                    }
                });
        snackbar.show();
    }

    @Override
    protected String getTitle() {
        return "Donate Blood";
    }
}
