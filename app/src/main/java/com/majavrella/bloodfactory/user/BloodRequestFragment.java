package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequestFragment extends UserFragment {

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
    @Bind(R.id.post_blood_request) Button mPostBloodRequest;

    public static BloodRequestFragment newInstance() {
        return new BloodRequestFragment();
    }



    public BloodRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_request, container, false);
    }

    @Override
    protected String getTitle() {
        return "Post Blood request";
    }
}
