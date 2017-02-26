package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AddMemberFragment extends UserFragment {

    private static View mAddMemberView;

    @Bind(R.id.name_error) TextView mNameError;
    @Bind(R.id.name_error_layout) LinearLayout mNameErrorLayout;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.mob_error) TextView mMobError;
    @Bind(R.id.mob_error_layout) LinearLayout mMobErrorLayout;
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
    @Bind(R.id.donar_state) Spinner mDonarState;
    @Bind(R.id.donar_city) Spinner mDonarCity;
    @Bind(R.id.availability_status) RadioGroup mAvailabilityStatus;
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

        mAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if (isValid){
                    Toast.makeText(mActivity, "Successfully added a member !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mAddMemberView;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(mDonarName.getText().toString().equals("")|| mDonarName.getText().toString().trim().length()<3){
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
        if(mDonarBloodGroup.getSelectedItem().toString().equals("--Select blood group--")){
            setErrorMsg(mBloodGrpErrorLayout, mBloodGrpError, "Select a blood group!");
            validation = false;
        } else { hideIt(mBloodGrpErrorLayout); }
        if(mDonarMob.getText().toString().equals("")||mDonarMob.getText().toString().trim().length()<10){
            setErrorMsg(mMobErrorLayout, mMobError, "Oops! Mobile no?");
            validation = false;
        } else { hideIt(mMobErrorLayout); }
        if(mDonarState.getSelectedItem().toString().equals("--select state--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Please select your State");
            validation = false;
        } else if(mDonarCity.getSelectedItem().toString().equals("--select city--")){
            setErrorMsg(mAddressErrorLayout, mAddressError, "Select your City");
            validation = false;
        } else {
            hideIt(mAddressErrorLayout);
        }
        return validation;
    }

    public void setErrorMsg(LinearLayout linearLayout,TextView tv, String msg){
        linearLayout.setVisibility(View.VISIBLE);
        tv.setText(msg);
    }

    public void setErrorMsgEmpty(){
        mNameError.setText("");
        mBloodGrpError.setText("");
        mMobError.setText("");
        mAddressError.setText("");
        mGenderError.setText("");
        mAgeError.setText("");
    }

    public void hideIt(LinearLayout ll){
        ll.setVisibility(View.GONE);

    }

    @Override
    protected String getTitle() {
        return "Add Member";
    }

}
