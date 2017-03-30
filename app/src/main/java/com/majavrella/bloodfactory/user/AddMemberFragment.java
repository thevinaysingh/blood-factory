package com.majavrella.bloodfactory.user;


import android.os.Bundle;
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

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.modal.Member;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AddMemberFragment extends UserFragment {

    private static View mAddMemberView;
    private static String name, gender, age, bloodGroup, mob, address, state, city, availability, authorization;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.address_error) TextView mAddressError;
    @Bind(R.id.address_error_layout) LinearLayout mAddressErrorLayout;
    @Bind(R.id.gender_error) TextView mGenderError;
    @Bind(R.id.gender_error_layout) LinearLayout mGenderErrorLayout;
    @Bind(R.id.age_error) TextView mAgeError;
    @Bind(R.id.age_error_layout) LinearLayout mAgeErrorLayout;

    @Bind(R.id.donar_name) EditText mDonarName;
    @Bind(R.id.gender_status) RadioGroup mGenderStatus;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.donar_blood_group) Spinner mDonarBloodGroup;
    @Bind(R.id.donar_mob) EditText mDonarMob;
    @Bind(R.id.donar_address) EditText mDonarAddress;
    @Bind(R.id.donar_state) Spinner mDonarState;
    @Bind(R.id.donar_city) Spinner mDonarCity;
    @Bind(R.id.availability_status) RadioGroup mAvailabilityStatus;
    @Bind(R.id.donar_authorization) CheckBox mDonarAuthorization;
    @Bind(R.id.add_member) Button mAddMember;

    public static AddMemberFragment newInstance() {
        return new AddMemberFragment();
    }

    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAddMemberView = inflater.inflate(R.layout.fragment_add_member, container, false);
        ButterKnife.bind(this, mAddMemberView);

        setStatusBarColor(Constants.colorStatusBarSecondary);
        mGenderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {hideIt(mGenderErrorLayout);}
        });
        mAgeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {hideIt(mAgeErrorLayout);}
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
        mAddMember.setOnClickListener(mAddMemberButtonListener);
        return mAddMemberView;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(name.equals("")||!isNameValid(name)){
            mDonarName.setError(Constants.nameErrorText);
            validation = false;
        }
        if(mGenderStatus.getCheckedRadioButtonId()<0){
            setErrorMsg(mGenderErrorLayout, mGenderError, Constants.genderErrorText);
            validation = false;
        }
        if (mAgeGroup.getCheckedRadioButtonId()<0){
            setErrorMsg(mAgeErrorLayout, mAgeError, Constants.ageErrorText);
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

    private View.OnClickListener mAddMemberButtonListener    =   new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetData();
            setDataInStringFormat();
            boolean isAllFieldsValid = dataValidation();
            if (isAllFieldsValid){
                Donar Donar = setDataInModal(new Donar());
                Toast.makeText(mActivity, "Successfully added a member !!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Donar setDataInModal(Donar donar) {
        donar.setName(name);
        donar.setGender(gender);
        donar.setAgeGroup(age);
        donar.setBloodGroup(bloodGroup);
        donar.setMobile(mob);
        donar.setAddress(address);
        donar.setState(state);
        donar.setCity(city);
        donar.setAvailability(availability);
        donar.setAuthorization(authorization);
        return donar;
    }

    private void resetData() {
        name = gender = age = bloodGroup = mob = address = state = city = availability = authorization = null;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mDonarName);
        if(mGenderStatus.getCheckedRadioButtonId()>=0){
            gender = getStringDataFromRadioButton((RadioButton) mAddMemberView.findViewById(mGenderStatus.getCheckedRadioButtonId()));
        }
        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            age = getStringDataFromRadioButton((RadioButton) mAddMemberView.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }
        bloodGroup = getStringDataFromSpinner(mDonarBloodGroup);
        mob = getStringDataFromEditText(mDonarMob);
        address = getStringDataFromEditText(mDonarAddress);
        state = getStringDataFromSpinner(mDonarState);
        city = getStringDataFromSpinner(mDonarCity);
        availability = getStringDataFromRadioButton((RadioButton) mAddMemberView.findViewById(mAvailabilityStatus.getCheckedRadioButtonId()));
        authorization = mDonarAuthorization.isChecked()? "True" : "False";
    }

    @Override
    protected String getTitle() {
        return Constants.kAddMemberFragment;
    }
}
