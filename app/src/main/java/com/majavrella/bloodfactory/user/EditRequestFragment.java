package com.majavrella.bloodfactory.user;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.modal.Patient;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONObject;

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
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.patient_blood_group) Spinner mPatientBloodGroup;
    @Bind(R.id.patient_mob) EditText mPatientMob;
    @Bind(R.id.patient_state) Spinner mPatientState;
    @Bind(R.id.patient_city) Spinner mPatientCity;
    @Bind(R.id.last_date_need) EditText mLastDateNeed;
    @Bind(R.id.purpose_of_request) EditText mPurposeOfRequest;
    @Bind(R.id.post_blood_request) Button mPostBloodRequest;

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
        /*editPatient = setDonar(mJsonObject, new Donar());

        mDonarState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    setCities(mDonarCity, parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });

        mEditButton.setOnClickListener(mEditRequestListener);*/
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return mEditRequest;
    }

/*
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
        name = getStringDataFromEditText(mDonarName);

        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            age = getStringDataFromRadioButton((RadioButton) mEditRequest.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }

        mob = getStringDataFromEditText(mDonarMob);
        address = getStringDataFromEditText(mDonarAddress);
        state = getStringDataFromSpinner(mDonarState);
        city = getStringDataFromSpinner(mDonarCity);
        availability = getStringDataFromRadioButton((RadioButton) mEditRequest.findViewById(mAvailabilityStatus.getCheckedRadioButtonId()));
        authorization = mDonarAuthorization.isChecked()? RegisterConstants.kTrue : RegisterConstants.kFalse;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(!isNameValid(name)){
            mDonarName.setError(Constants.nameErrorText);
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
            state = editPatient.getState();
        } if(city.equals("--Select city--")){
            city = editPatient.getCity();
        }
        return validation;
    }

    private void editDataOnCloud() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you really want to edit, if yes click ok and start processing...")
                .setTitle("Edit page")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progress.setMessage(RegisterConstants.waitProgress);
                        progress.show();
                        try {
                            editPatient.setName(name);
                            editPatient.setMobile(mob);
                            editPatient.setAddress(address);
                            editPatient.setCity(city);
                            editPatient.setState(state);
                            editPatient.setAgeGroup(age);
                            editPatient.setAvailability(availability);
                            editPatient.setAuthorization(authorization);
                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
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
        mDonarName.setText(editPatient.getName());
        mDonarMob.setText(editPatient.getMobile());
        mDonarAddress.setText(editPatient.getAddress());
        mGenderStatus.setText(editPatient.getGender());
        mDonarBloodGroup.setText(editPatient.getBloodGroup());
        mStateCity.setText(editPatient.getCity()+", "+editPatient.getState());
        if(editPatient.getAgeGroup().equals("18+")){
            mAbove18.setChecked(true);
        } else {
            mAbove35.setChecked(true);
        }
        if(editPatient.getAvailability().equals("Unavailable")){
            mDonarInactive.setChecked(true);
        } else {
            mDonarActive.setChecked(true);
        }
    }*/

    @Override
    protected String getTitle() {
        return "Edit page";
    }
}
