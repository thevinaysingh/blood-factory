package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends UserFragment {

    public View directDonateFragment;
    Spinner donarBloodGroup,donarCity,donarState;
    EditText donarName, donarDob, donarContact, donarAddress;
    CheckBox  donarAuthorization;
    RadioGroup donarSexGroup, donarStatus;
    RadioButton donarSex, statusButton;
    Button donateNow;

    public static DonateFragment newInstance() {
        return new DonateFragment();
    }

    public DonateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        directDonateFragment = inflater.inflate(R.layout.fragment_donate, container, false);

        donarBloodGroup = (Spinner) directDonateFragment.findViewById(R.id.donarBloodGroup);
        donarCity = (Spinner) directDonateFragment.findViewById(R.id.donarCity);
        donarState = (Spinner) directDonateFragment.findViewById(R.id.donarState);
        donarName = (EditText) directDonateFragment.findViewById(R.id.donarName);
        donarDob = (EditText) directDonateFragment.findViewById(R.id.donarDob);
        donarContact = (EditText) directDonateFragment.findViewById(R.id.donarMob);
        donarAddress = (EditText) directDonateFragment.findViewById(R.id.donarAddress);
        donarAuthorization= (CheckBox) directDonateFragment.findViewById(R.id.donarAuthorization);
        donarSexGroup = (RadioGroup) directDonateFragment.findViewById(R.id.genderStatus);
        donarStatus = (RadioGroup) directDonateFragment.findViewById(R.id.donarStatus);
        donateNow = (Button) directDonateFragment.findViewById(R.id.directDonateBtn);
        donateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if(isValid){
                    String name = donarName.getText().toString();
                    String dob = donarDob.getText().toString();
                    int genderId = donarSexGroup.getCheckedRadioButtonId();
                    String gender;
                    donarSex = (RadioButton) directDonateFragment.findViewById(genderId);
                    if(donarSex.getText().toString().equals("M")){
                        gender = "Male";
                    }else{
                        gender = "Male";
                    }
                    String mob = donarContact.getText().toString();
                    String bloodGroup = donarBloodGroup.getSelectedItem().toString() ;
                    String address = donarAddress.getText().toString();
                    String city = donarCity.getSelectedItem().toString();
                    String state = donarState.getSelectedItem().toString();
                    int statusId = donarStatus.getCheckedRadioButtonId();
                    statusButton = (RadioButton) directDonateFragment.findViewById(statusId);
                    String status ;
                    if(statusButton.getText().toString().equals("Availble")){
                        status = "Active";
                    }else {
                        status = "Inactive";
                    }
                    String authorizationCall;
                    if(donarAuthorization.isChecked()){
                        authorizationCall = "true";
                    } else{
                        authorizationCall = "false";
                    }

                    Toast.makeText(mActivity, "Successfully saved !!!", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(mActivity, "Please fill all data field !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return directDonateFragment;
    }

    private boolean dataValidation() {
       boolean validation = false;
        if(donarName.getText().toString().equals("")){
            validation = false;
        } else if(donarDob.getText().toString().equals("")){
            validation = false;
        } else if(donarContact.getText().toString().equals("")){
            validation = false;
        } else if(donarAddress.getText().toString().equals("")){
            validation = false;
        } else if(!donarDob.getText().toString().contains("/")||donarDob.getText().toString().length()<10){
            validation = false;
        } else if(donarContact.getText().toString().length()<10){
            validation = false;
        } else if(donarBloodGroup.getSelectedItem().toString().equals("--Select blood group--")){
            validation = false;
        } else if(donarCity.getSelectedItem().toString().equals("--select city--")){
            validation = false;
        } else if(donarState.getSelectedItem().toString().equals("--select state--")){
            validation = false;
        } else if(donarSexGroup.getCheckedRadioButtonId()<0){
            validation = false;
        } else{
            validation = true;
        }
        return validation;
    }

    @Override
    protected String getTitle() {
        return "Donate Blood";
    }
}
