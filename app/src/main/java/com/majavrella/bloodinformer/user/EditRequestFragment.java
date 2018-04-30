package com.majavrella.bloodinformer.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserFragment;
import com.majavrella.bloodinformer.modal.Patient;
import com.majavrella.bloodinformer.register.RegisterConstants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 4/25/2017.
 */

public class EditRequestFragment extends UserFragment {

    private static View mEditRequest;
    private static String name, gender, ageGroup, bloodGroup, mob, state, city, lastDate, purpose;
    private static JSONObject mJsonObject;
    private static Patient editPatient;

    @Bind(R.id.patient_name) EditText mPatientName;
    @Bind(R.id.gender_status) RadioGroup mGenderStatus;
    @Bind(R.id.genderFemale) RadioButton mGenderFemale;
    @Bind(R.id.genderMale) RadioButton mGenderMale;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.above18) RadioButton mAbove18;
    @Bind(R.id.above35) RadioButton mAbove35;
    @Bind(R.id.patient_blood_group_edit) TextView mPatient_blood_group_edit;
    @Bind(R.id.patient_blood_group) Spinner mPatientBloodGroup;
    @Bind(R.id.patient_mob) EditText mPatientMob;
    @Bind(R.id.saved_state_city) TextView mSaved_state_city;
    @Bind(R.id.patient_state) Spinner mPatientState;
    @Bind(R.id.patient_city) Spinner mPatientCity;
    @Bind(R.id.last_date_need) EditText mLastDateNeed;
    @Bind(R.id.purpose_of_request) EditText mPurposeOfRequest;
    @Bind(R.id.edit_button) Button mEditButton;

    @Bind(R.id.goto_back) Button mGoBackButton;
    @Bind(R.id.success_page) LinearLayout success_page;
    @Bind(R.id.edit_container) LinearLayout edit_container;


    public static EditRequestFragment newInstance(JSONObject jsonObject) {
        mJsonObject = jsonObject;
        return new EditRequestFragment();
    }

    public EditRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEditRequest = inflater.inflate(R.layout.edit_request_fragment, container, false);
        ButterKnife.bind(this, mEditRequest);
        setStatusBarColor(Constants.colorStatusBarSecondary);
        editPatient = setPatient(mJsonObject, new Patient());

        mPatientState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                setCities(mPatientCity, parent.getItemAtPosition(position).toString());
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


        mEditButton.setOnClickListener(mEditRequestListener);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return mEditRequest;
    }

    View.OnClickListener mEditRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetModalData();
                setDataInStringFormat();
                if(dataValidation()){
                    editDataOnCloud();
                } else {
                    Toast.makeText(mActivity, "Input error", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNetworkError(mEditRequest, RegisterConstants.networkErrorText);
            }
        }
    };

    private void resetModalData() {
        name = gender = ageGroup = bloodGroup = mob = state = city = lastDate = purpose = null;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mPatientName);
        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            ageGroup = getStringDataFromRadioButton((RadioButton) mEditRequest.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }

        if(mGenderStatus.getCheckedRadioButtonId()>=0){
            gender = getStringDataFromRadioButton((RadioButton) mEditRequest.findViewById(mGenderStatus.getCheckedRadioButtonId()));
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
        if(!isNameValid(name)){
            mPatientName.setError(Constants.nameErrorText);
            validation = false;
        }

        if(bloodGroup.equals("--Select blood group--")){
            bloodGroup = editPatient.getBloodGroup();
        }

        if(mob.equals("")||!isPhoneValid(mob)){
            mPatientMob.setError(Constants.mobErrorText);
            validation = false;
        }

        if(state.equals("--Select state--")){
            city = editPatient.getCity();
            state = editPatient.getState();
        } else {
            if(city.equals("--Select city--")){
                showNetworkError(mEditRequest, "Select city");
                validation = false;
            }
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
        if(purpose.equals("")){
            purpose = editPatient.getPurpose();
        }
        return validation;
    }

    private void editDataOnCloud() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you really want to edit, if yes click ok and start processing...")
                .setTitle("Edit request")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progress.setMessage(RegisterConstants.waitProgress);
                        progress.show();
                        progress.setCancelable(false);
                        try {
                            editPatient.setAgeGroup(ageGroup);
                            editPatient.setBloodGroup(bloodGroup);
                            editPatient.setCity(city);
                            editPatient.setDate(lastDate);
                            editPatient.setGender(gender);
                            editPatient.setMobile(mob);
                            editPatient.setName(name);
                            editPatient.setPurpose(purpose);
                            editPatient.setState(state);
                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.patients_db);
                            mDonarsDatabase.child(editPatient.getSelfRefKey()).setValue(editPatient);
                            Toast.makeText(mActivity, "Editing done", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            showSuccessMsg();
                        } catch (Exception e){
                            e.printStackTrace();
                            progress.dismiss();
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
        success_page.setVisibility(View.VISIBLE);
        edit_container.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        setDonarsDataToEdit();
        super.onResume();
    }

    private void setDonarsDataToEdit() {
        mPatientName.setText(editPatient.getName());
        mPatientMob.setText(editPatient.getMobile());

        if(editPatient.getAgeGroup().equals("18+")){
            mAbove18.setChecked(true);
            mAbove35.setChecked(false);
        } else {
            mAbove35.setChecked(true);
            mAbove18.setChecked(false);
        }
        if(editPatient.getGender().equals("Male")){
            mGenderMale.setChecked(true);
            mGenderFemale.setChecked(false);
        } else {
            mGenderMale.setChecked(false);
            mGenderFemale.setChecked(true);
        }
        mPatient_blood_group_edit.setText(editPatient.getBloodGroup());
        mSaved_state_city.setText(editPatient.getCity()+", "+editPatient.getState());
        mLastDateNeed.setText(editPatient.getDate());
        mPurposeOfRequest.setText(editPatient.getPurpose());
    }

    @Override
    protected String getTitle() {
        return "Edit Request";
    }
}
