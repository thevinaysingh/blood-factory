package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Patient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequestFragment extends UserFragment {

    private static View mBloodRequestView;

    @Bind(R.id.name_error) TextView mNameError;
    @Bind(R.id.name_error_layout) LinearLayout mNameErrorLayout;
    @Bind(R.id.gender_error) TextView mGenderError;
    @Bind(R.id.gender_error_layout) LinearLayout mGenderErrorLayout;
    @Bind(R.id.age_error) TextView mAgeError;
    @Bind(R.id.age_error_layout) LinearLayout mAgeErrorLayout;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.mob_error) TextView mMobError;
    @Bind(R.id.mob_error_layout) LinearLayout mMobErrorLayout;
    @Bind(R.id.address_error) TextView mAddressError;
    @Bind(R.id.address_error_layout) LinearLayout mAddressErrorLayout;
    @Bind(R.id.last_date_need_error) TextView mLastDateNeedError;
    @Bind(R.id.last_date_need_error_layout) LinearLayout mLastDateNeedErrorLayout;

    @Bind(R.id.patient_name) EditText mPatientName;
    @Bind(R.id.gender_status) RadioGroup mGenderStatus;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.patient_blood_group) Spinner mPatientBloodGroup;
    @Bind(R.id.patient_mob) EditText mPatientMob;
    @Bind(R.id.patient_state) Spinner mPatientState;
    @Bind(R.id.patient_city) Spinner mPatientCity;
    @Bind(R.id.last_date_need) EditText mLastDateNeed;
    @Bind(R.id.purpose_of_request) EditText mPurposeOfRequest;
    @Bind(R.id.post_blood_request) Button mPostBloodRequest;

    public static BloodRequestFragment newInstance() {
        return new BloodRequestFragment();
    }

    public BloodRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBloodRequestView = inflater.inflate(R.layout.fragment_blood_request, container, false);
        ButterKnife.bind(this, mBloodRequestView);

        mPostBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if (isValid){
                    Toast.makeText(mActivity, "Successfully added a member !!!", Toast.LENGTH_SHORT).show();
                    setData();
                }
            }
        });

        mPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>2){
                    hideIt(mNameErrorLayout);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mGenderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {hideIt(mGenderErrorLayout);}
        });
        mPatientBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mPatientState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mPatientCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mPatientMob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideIt(mMobErrorLayout);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mLastDateNeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideIt(mLastDateNeedErrorLayout);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return mBloodRequestView;
    }

    private void setData() {
        Patient patient = new Patient();
        patient.setName(mPatientName.getText().toString().trim());
        RadioButton patientSex = (RadioButton) mBloodRequestView.findViewById(mGenderStatus.getCheckedRadioButtonId());
        patient.setGender(patientSex.getText().toString());
        RadioButton patientAge = (RadioButton) mBloodRequestView.findViewById(mAgeGroup.getCheckedRadioButtonId());
        patient.setAgeGroup(patientAge.getText().toString());
        patient.setBloodGroup(mPatientBloodGroup.getSelectedItem().toString());
        patient.setMobile(mPatientMob.getText().toString().trim());
        patient.setState(mPatientState.getSelectedItem().toString());
        patient.setCity(mPatientCity.getSelectedItem().toString());
        patient.setDate(mLastDateNeed.getText().toString().trim());
        patient.setPurpose(mPurposeOfRequest.getText().toString());
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(mPatientName.getText().toString().equals("")|| mPatientName.getText().toString().trim().length()<3){
            setErrorMsg(mNameErrorLayout, mNameError, "Good name? Please!");
            validation = false;
        } else { hideIt(mNameErrorLayout); }
        if(mGenderStatus.getCheckedRadioButtonId()<0){
            setErrorMsg(mGenderErrorLayout, mGenderError, "Who is?");
            validation = false;
        } else { hideIt(mGenderErrorLayout); }
        if (mAgeGroup.getCheckedRadioButtonId()<0){
            setErrorMsg(mAgeErrorLayout, mAgeError, "Age group? Please");
            validation = false;
        } else { hideIt(mAgeErrorLayout); }
        if(mPatientBloodGroup.getSelectedItem().toString().equals("--Select blood group--")){
            setErrorMsg(mBloodGrpErrorLayout, mBloodGrpError, "Select a blood group!");
            validation = false;
        } else { hideIt(mBloodGrpErrorLayout); }
        if(mPatientMob.getText().toString().equals("")||mPatientMob.getText().toString().trim().length()<10){
            setErrorMsg(mMobErrorLayout, mMobError, "Oops! Mobile no?");
            validation = false;
        } else { hideIt(mMobErrorLayout); }
        if(mPatientState.getSelectedItem().toString().equals("--select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Please select your State");
            validation = false;
        } else if(mPatientCity.getSelectedItem().toString().equals("--select city--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Select your City");
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        if(mLastDateNeed.getText().toString().equals("")){
            setErrorMsg(mLastDateNeedErrorLayout, mLastDateNeedError, "Last date of need is missing!");
            validation = false;
        } else if (mLastDateNeed.getText().toString().trim().length()<10||!mLastDateNeed.getText().toString().contains("/")){
            setErrorMsg(mLastDateNeedErrorLayout, mLastDateNeedError, "Date format is not good!");
            validation = false;
        } else { hideIt(mLastDateNeedErrorLayout); }
        return validation;
    }

    @Override
    protected String getTitle() {
        return "Post Blood request";
    }
}
