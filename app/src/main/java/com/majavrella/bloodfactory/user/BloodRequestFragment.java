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
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.modal.Member;
import com.majavrella.bloodfactory.modal.Patient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequestFragment extends UserFragment {

    private static View mBloodRequestView;
    private static String name, gender, ageGroup, bloodGroup, mob, state, city, lastDate, purpose;

    @Bind(R.id.gender_error) TextView mGenderError;
    @Bind(R.id.gender_error_layout) LinearLayout mGenderErrorLayout;
    @Bind(R.id.age_error) TextView mAgeError;
    @Bind(R.id.age_error_layout) LinearLayout mAgeErrorLayout;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.address_error) TextView mAddressError;
    @Bind(R.id.address_error_layout) LinearLayout mAddressErrorLayout;

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

        setStatusBarColor(Constants.colorStatusBarSecondary);
        mGenderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {hideIt(mGenderErrorLayout);}
        });
        mAgeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {hideIt(mAgeErrorLayout);}
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

        mPostBloodRequest.setOnClickListener(mPostBloodRequestListener);
        return mBloodRequestView;
    }

    private void setData() {
        Patient patient = new Patient();
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(name.equals("")|| !isNameValid(name)){
            mPatientName.setError(Constants.nameErrorText);
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
            mPatientMob.setError(Constants.mobErrorText);
            validation = false;
        }
        if(state.equals("--select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, Constants.stateErrorText);
            validation = false;
        } else if(city.equals("--select city--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, Constants.cityErrorText);
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        if(!isDateValid(lastDate)){
            mLastDateNeed.setError(Constants.dateErrorText);
            validation = false;
        }
        if(purpose==null){
            purpose = "Purpose not given!";
        }
        return validation;
    }

    View.OnClickListener mPostBloodRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetModalData();
            setDataInStringFormat();
            boolean isAllFieldsValid =  dataValidation();
            if (isAllFieldsValid){
                Patient patient = setDataInModal(new Patient());
                Toast.makeText(mActivity, "Successfully added a member !!!", Toast.LENGTH_SHORT).show();
                setData();
            }
        }
    };

    private Patient setDataInModal(Patient patient) {
        patient.setName(name);
        patient.setGender(gender);
        patient.setAgeGroup(ageGroup);
        patient.setBloodGroup(bloodGroup);
        patient.setMobile(mob);
        patient.setState(state);
        patient.setCity(city);
        patient.setDate(lastDate);
        patient.setPurpose(purpose);
        return patient;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mPatientName);
        if(mGenderStatus.getCheckedRadioButtonId()>=0){
            gender = getStringDataFromRadioButton((RadioButton) mBloodRequestView.findViewById(mGenderStatus.getCheckedRadioButtonId()));
        }
        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            ageGroup = getStringDataFromRadioButton((RadioButton) mBloodRequestView.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }
        bloodGroup = getStringDataFromSpinner(mPatientBloodGroup);
        mob = getStringDataFromEditText(mPatientMob);
        state = getStringDataFromSpinner(mPatientState);
        city = getStringDataFromSpinner(mPatientCity);
        lastDate= getStringDataFromEditText(mLastDateNeed);
        purpose = getStringDataFromEditText(mPurposeOfRequest);
    }

    private void resetModalData() {
        name = gender = ageGroup = bloodGroup = mob = state = city = lastDate = purpose = null;
    }

    @Override
    protected String getTitle() {
        return Constants.kBloodRequestFragment;
    }
}
