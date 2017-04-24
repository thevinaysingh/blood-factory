package com.majavrella.bloodfactory.user;

import android.content.Context;
import android.content.DialogInterface;
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
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.register.RegisterConstants;

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

    @Bind(R.id.donar_name) EditText mDonarName;
    @Bind(R.id.donar_mob) EditText mDonarMob;
    @Bind(R.id.donar_address) EditText mDonarAddress;
    @Bind(R.id.donar_state) Spinner mDonarState;
    @Bind(R.id.donar_city) Spinner mDonarCity;
    @Bind(R.id.gender_status) TextView mGenderStatus;
    @Bind(R.id.donar_blood_group) TextView mDonarBloodGroup;
    @Bind(R.id.age_group) RadioGroup mAgeGroup;
    @Bind(R.id.donar_status) RadioGroup mAvailabilityStatus;
    @Bind(R.id.donar_authorization) CheckBox mDonarAuthorization;
    @Bind(R.id.edit_button) Button mEditButton;

    public static EditFragment newInstance() {
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
                    progress.setMessage(RegisterConstants.waitProgress);
                    progress.show();
                    try {
                        progress.dismiss();
                    } catch (Exception e){
                        e.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(mActivity, "Operation failed", Toast.LENGTH_SHORT).show();
                    }
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
        authorization = mDonarAuthorization.isChecked()? "True" : "False";
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
            validation = false;
        } else if(city.equals("--Select city--")){
            validation = false;
        }
        return validation;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return "Edit page";
    }
}
