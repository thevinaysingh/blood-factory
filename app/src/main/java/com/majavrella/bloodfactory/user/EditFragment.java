package com.majavrella.bloodfactory.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.BackButtonSupportFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 4/25/2017.
 */

public class EditFragment extends UserFragment {

    private static View mEditFragment;
    private static String name, age, mob, address, state, city, availability, authorization;
    private static JSONObject mJsonObject;
    private static Donar editDonar;

    @Bind(R.id.donar_name) EditText mDonarName;
    @Bind(R.id.donar_mob) EditText mDonarMob;
    @Bind(R.id.donar_address) EditText mDonarAddress;
    @Bind(R.id.donar_state) Spinner mDonarState;
    @Bind(R.id.donar_city) Spinner mDonarCity;
    @Bind(R.id.saved_state_city) TextView mStateCity;
    @Bind(R.id.gender_status) TextView mGenderStatus;
    @Bind(R.id.donar_blood_group) TextView mDonarBloodGroup;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.above18) RadioButton mAbove18;
    @Bind(R.id.above35) RadioButton mAbove35;
    @Bind(R.id.donar_status) RadioGroup mAvailabilityStatus;
    @Bind(R.id.donarActive) RadioButton mDonarActive;
    @Bind(R.id.donarInactive) RadioButton mDonarInactive;
    @Bind(R.id.donar_authorization) CheckBox mDonarAuthorization;
    @Bind(R.id.edit_button) Button mEditButton;

    @Bind(R.id.goto_back) Button mGoBackButton;
    @Bind(R.id.success_page) LinearLayout success_page;
    @Bind(R.id.edit_container) LinearLayout edit_container;


    public static EditFragment newInstance(JSONObject jsonObject) {
        mJsonObject = jsonObject;
        return new EditFragment();
    }

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEditFragment = inflater.inflate(R.layout.edit_fragment, container, false);
        ButterKnife.bind(this, mEditFragment);
        setStatusBarColor(Constants.colorStatusBarSecondary);
        editDonar = setDonar(mJsonObject, new Donar());

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

        mEditButton.setOnClickListener(mEditFragmentListener);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return mEditFragment;
    }


    View.OnClickListener mEditFragmentListener = new View.OnClickListener() {
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
                showNetworkError(mEditFragment, RegisterConstants.networkErrorText);
            }
        }
    };

    private void resetModalData() {
        name  = age  = mob = address = state = city = availability = authorization = null;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mDonarName);

        if(mAgeGroup.getCheckedRadioButtonId()>=0){
            age = getStringDataFromRadioButton((RadioButton) mEditFragment.findViewById(mAgeGroup.getCheckedRadioButtonId()));
        }

        mob = getStringDataFromEditText(mDonarMob);
        address = getStringDataFromEditText(mDonarAddress);
        state = getStringDataFromSpinner(mDonarState);
        city = getStringDataFromSpinner(mDonarCity);
        availability = getStringDataFromRadioButton((RadioButton) mEditFragment.findViewById(mAvailabilityStatus.getCheckedRadioButtonId()));
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
            state = editDonar.getState();
        } if(city.equals("--Select city--")){
            city = editDonar.getCity();
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
                            editDonar.setName(name);
                            editDonar.setMobile(mob);
                            editDonar.setAddress(address);
                            editDonar.setCity(city);
                            editDonar.setState(state);
                            editDonar.setAgeGroup(age);
                            editDonar.setAvailability(availability);
                            editDonar.setAuthorization(authorization);
                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
                            mDonarsDatabase.child(editDonar.getSelfRefKey()).setValue(editDonar);
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
        mDonarName.setText(editDonar.getName());
        mDonarMob.setText(editDonar.getMobile());
        mDonarAddress.setText(editDonar.getAddress());
        mGenderStatus.setText(editDonar.getGender());
        mDonarBloodGroup.setText(editDonar.getBloodGroup());
        mStateCity.setText(editDonar.getCity()+", "+editDonar.getState());
        if(editDonar.getAgeGroup().equals("18+")){
            mAbove18.setChecked(true);
        } else {
            mAbove35.setChecked(true);
        }
        if(editDonar.getAvailability().equals("Unavailable")){
            mDonarInactive.setChecked(true);
        } else {
            mDonarActive.setChecked(true);
        }
    }

    @Override
    protected String getTitle() {
        return "Edit page";
    }
}
