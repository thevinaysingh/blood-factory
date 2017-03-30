package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends UserFragment {

    private static View mDonateFragment;
    private static String name, gender, age, bloodGroup, mob, address, state, city, availability, authorization;

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

        setStatusBarColor(Constants.colorStatusBarSecondary);
        mGenderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) { hideIt(mGenderErrorLayout); }
        });
        mDonarBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    hideIt(mBloodGrpErrorLayout);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mDonarState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    hideIt(mAddressErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDonarCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    hideIt(mAddressErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDonateButton.setOnClickListener(mDonateButtonListener);
        return mDonateFragment;
    }

    private Donar setDataInModal(Donar donar) {
        donar.setName(name);
        donar.setGender(gender);
        donar.setBloodGroup(bloodGroup);
        donar.setAgeGroup(age);
        donar.setMobile(mob);
        donar.setAddress(address);
        donar.setState(state);
        donar.setCity(city);
        donar.setAvailability(availability);
        donar.setAuthorization(authorization);
        return donar;
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

    private void resetModalData() {
        name = gender = age = bloodGroup = mob = address = state = city = availability = authorization = null;
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
    }

    View.OnClickListener mDonateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetModalData();
            setDataInStringFormat();
            boolean isAllFieldsValid = dataValidation();
            if(isAllFieldsValid){
                progress.setMessage("Saving donar...");
                progress.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try{
                            Donar donar = setDataInModal(new Donar());
                            Toast.makeText(mActivity, "Now, You're able to donate!!!", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mActivity, "Operation failed!!!", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                }, 2000);

            }else {
                Toast.makeText(mActivity, "Something went wrong !!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected String getTitle() {
        return "Donate Blood";
    }
}
