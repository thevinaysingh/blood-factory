package com.majavrella.bloodfactory.user;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.modal.Member;
import com.majavrella.bloodfactory.modal.Patient;
import com.majavrella.bloodfactory.register.RegisterConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

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
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideKeyboard(getActivity());
                hideIt(mGenderErrorLayout);}
        });
        mAgeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideKeyboard(getActivity());
                hideIt(mAgeErrorLayout);}
        });
        mPatientBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mPatientState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mAddressErrorLayout);
                }
                setCities(mPatientCity, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });
        mPatientCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mLastDateNeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("-----", "beforeTextChanged: "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(mActivity, "Length"+s.length()+ "start"+start+ " before"+ before+ " count"+ count , Toast.LENGTH_SHORT).show();
                if(mLastDateNeed.getText().length()==2 && start==1){
                    mLastDateNeed.setText(s+"/");
                }
                if(mLastDateNeed.getText().length()==5 && start==4){
                    mLastDateNeed.setText(s+"/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mLastDateNeed.getText().length()==3||mLastDateNeed.getText().length()==6)
                mLastDateNeed.setSelection(mLastDateNeed.getText().length());
            }
        });

        mPostBloodRequest.setOnClickListener(mPostBloodRequestListener);
        return mBloodRequestView;
    }

    View.OnClickListener mPostBloodRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetModalData();
                setDataInStringFormat();
                if(dataValidation()){
                    progress.setMessage(RegisterConstants.waitProgress);
                    progress.show();
                    try{
                        setDataOnCloud();
                    } catch(Exception e){
                        e.printStackTrace();
                        progress.dismiss();
                    }
                } else {
                    Toast.makeText(mActivity, "Required data entry", Toast.LENGTH_SHORT).show();
                }
            } else {
                showSnackbar(mBloodRequestView, RegisterConstants.networkErrorText);
            }
        }
    };

    private void resetModalData() {
        name = gender = ageGroup = bloodGroup = mob = state = city = lastDate = purpose = null;
    }

    private void setDataInStringFormat() {
        name = capitalizeFirstLetter(getStringDataFromEditText(mPatientName));
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
        if(state.equals("--Select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, Constants.stateErrorText);
            validation = false;
        } else if(city.equals("--Select city--")||city.equals("--Select--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, Constants.cityErrorText);
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        if(!isDateValid(lastDate)){
            mLastDateNeed.setError(Constants.dateErrorText);
            validation = false;
        } else {
            Date currentDate = null;
            Date enteredDate = null;
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                currentDate = mdformat.parse(getCurrentDate());
                enteredDate = mdformat.parse(lastDate);
                if(enteredDate.before(currentDate)){
                    mLastDateNeed.setError(Constants.dateErrorText);
                    validation = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(purpose==null||purpose.equals("")){
            purpose = "Purpose not given!";
        }
        return validation;
    }

    private void setDataOnCloud() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try{
                    DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.patients_db);
                    String temp_key = mDonarsDatabase.push().getKey();
                    Patient patient = setDataInModal(new Patient());
                    patient.setSelfRefKey(temp_key);
                    mDonarsDatabase.child(temp_key).setValue(patient);
                    progress.dismiss();
                    resetAllField();
                    resetModalData();
                    showSuccessDialog("Post blood request", "You have successfully posted a blood request. will be responding you soon. ");
                }catch (Exception e){
                    Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        }, 2000);
    }

    private void resetAllField() {
        mPatientName.setText("");
        mPatientMob.setText("");
        mPurposeOfRequest.setText("");
        mLastDateNeed.setText("");
        setCities(mPatientCity, "--Select--");
        resetState(mPatientState);
        resetBlood(mPatientBloodGroup);
    }

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
        patient.setUserId(getCurrentUserId());
        return patient;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kBloodRequestFragment;
    }
}
