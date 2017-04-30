package com.majavrella.bloodfactory.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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

        getActivity().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setStatusBarColor(Constants.colorStatusBarSecondary);
        mBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mStateErrorLayout);
                }
                setCities(mCity, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });
        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    hideIt(mCityErrorLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });

        mFindBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()) {
                    resetModalData();
                    setDataInStringFormat();
                    if(dataValidation()){
                        progressDialog.setMessage(RegisterConstants.waitProgress);
                        progressDialog.show();
                        final String url = Constants.kBaseUrl+Constants.kDonars;
                        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                            @Override
                            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                                switch (code) {
                                    case API_SUCCESS:
                                        JSONArray jsonArray = searchResultInJson(json);
                                        add(DonarList.newInstance(jsonArray));
                                        break;
                                    case API_FAIL:
                                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                                        break;
                                    case API_NETWORK_FAIL:
                                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                                        break;
                                    default : {
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toast.makeText(mActivity, "Please fill all fields", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    showSnackbar(mRecieveFragment, RegisterConstants.networkErrorText);
                }
            }
        });
        return mRecieveFragment;
    }

    private JSONArray searchResultInJson(JSONObject json) {
        JSONArray requiredJson = new JSONArray();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get("bloodGroup").toString().equals(bloodGroup)
                        &&json.getJSONObject(key).get("state").toString().equals(state)
                        &&json.getJSONObject(key).get("city").toString().equals(city)
                        &&json.getJSONObject(key).get("authorization").toString().equals(RegisterConstants.kTrue)){
                    requiredJson.put(json.getJSONObject(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requiredJson;
    }

    private void setDataInStringFormat() {
        bloodGroup = getStringDataFromSpinner(mBloodGroup);
        state = getStringDataFromSpinner(mState);
        city = getStringDataFromSpinner(mCity);
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(bloodGroup.equals("--Select blood group--")){
            setErrorMsg(mBloodGrpErrorLayout, mBloodGrpError, Constants.bloodGroupErrorText);
            validation = false;
        }
        if(state.equals("--Select state--")){
            setErrorMsg(mStateErrorLayout, mStateError, Constants.stateErrorText);
            validation = false;
        }
        if(city.equals("--Select city--")){
            setErrorMsg(mCityErrorLayout, mCityError, Constants.cityErrorText);
            validation = false;
        }
        return validation;
    }

    private void resetModalData() {
        bloodGroup = state = city = null;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kSearchBloodFragment;
    }
}
