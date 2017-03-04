package com.majavrella.bloodfactory.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecieveFragment extends UserFragment {

    private static View mRecieveFragment;
    private static String bloodGroup, state, city;
    @Bind(R.id.blood_grp_error) TextView mBloodGrpError;
    @Bind(R.id.blood_grp_error_layout) LinearLayout mBloodGrpErrorLayout;
    @Bind(R.id.state_error) TextView mStateError;
    @Bind(R.id.state_error_layout) LinearLayout mStateErrorLayout;
    @Bind(R.id.city_error) TextView mCityError;
    @Bind(R.id.city_error_layout) LinearLayout mCityErrorLayout;

    @Bind(R.id.blood_group) Spinner mBloodGroup;
    @Bind(R.id.state) Spinner mState;
    @Bind(R.id.city) Spinner mCity;
    @Bind(R.id.find_blood) Button mFindBlood;

    public static RecieveFragment newInstance() {
        return new RecieveFragment();
    }

    public RecieveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecieveFragment = inflater.inflate(R.layout.fragment_recieve, container, false);
        ButterKnife.bind(this,mRecieveFragment);

        mBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    hideIt(mStateErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    hideIt(mCityErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFindBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
                setDataInStringFormat();
                boolean isAllFieldsValid = dataValidation();
                if(isAllFieldsValid){
                    DonarList donarList = new DonarList();
                    if(donarList!=null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frag_container, donarList).addToBackStack(null).commit();
                    }
                    Toast.makeText(mActivity, "Searching ... !!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, "Please fill all fields!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return mRecieveFragment;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(bloodGroup.equals("--Select blood group--")){
            setErrorMsg(mBloodGrpErrorLayout, mBloodGrpError, Constants.bloodGroupErrorText);
            validation = false;
        }
        if(state.equals("--select state--")){
            setErrorMsg(mStateErrorLayout, mStateError, Constants.stateErrorText);
            validation = false;
        }
        if(city.equals("--select city--")){
            setErrorMsg(mCityErrorLayout, mCityError, Constants.cityErrorText);
            validation = false;
        }
        return validation;
    }

    private void resetData() {
        bloodGroup = state = city = null;
    }

    private void setDataInStringFormat() {
        bloodGroup = getStringDataFromSpinner(mBloodGroup);
        state = getStringDataFromSpinner(mState);
        city = getStringDataFromSpinner(mCity);
    }

    @Override
    protected String getTitle() {
        return Constants.kSearchBloodFragment;
    }
}
