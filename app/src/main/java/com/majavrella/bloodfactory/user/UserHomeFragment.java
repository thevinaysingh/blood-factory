package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.BackButtonSupportFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Patient;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHomeFragment extends UserFragment implements BackButtonSupportFragment {

    private static View userHomeFragment;
    private boolean consumingBackPress = true;
    private final String TAG = "UserHomeFragment";
    private Toast toast;
    protected SharedPreferences mSharedpreferences;
    @Bind(R.id.donate_blood) LinearLayout mDonateButton;
    @Bind(R.id.find_blood) LinearLayout mFindButton;

    @Bind(R.id.request_info_container) LinearLayout mRequestInfoContainer;
    @Bind(R.id.request_container) LinearLayout mRequestContainer;

    public static UserHomeFragment newInstance() {
        return new UserHomeFragment();
    }

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userHomeFragment = inflater.inflate(R.layout.fragment_user_home, container, false);
        ButterKnife.bind(this, userHomeFragment);

        mSharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        setStatusBarColor(Constants.colorStatusBar);
        mDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(DonateFragment.newInstance());
            }
        });
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(RecieveFragment.newInstance());
            }
        });

        return userHomeFragment;
    }

    @Override
    public void onStart() {
        progress.setMessage(RegisterConstants.waitProgress);
        progress.show();
        fetchRequests();
        super.onStart();
    }

    private void fetchRequests() {
        if(isNetworkAvailable()){

            final String url = Constants.kBaseUrl+Constants.kPatients;
            APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                @Override
                public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                    switch (code) {
                        case API_SUCCESS:
                            JSONArray jsonArray = searchResultInJson(json);
                            try {
                                createListOfRequests(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case API_FAIL:
                            progress.dismiss();
                            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                            break;
                        case API_NETWORK_FAIL:
                            progress.dismiss();
                            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                            break;
                    }
                }
            });
        } else {
            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
        }
    }

    private void createListOfRequests(final JSONArray mRequestsArray) throws JSONException {
        if(mRequestsArray.length()>0){
            mRequestInfoContainer.setVisibility(View.VISIBLE);
            for(int i = 0; i<mRequestsArray.length(); i++){
                Patient patient = null;
                JSONObject json_data = null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_response, null);
                ImageView memberImg = (ImageView) view.findViewById(R.id.member_image);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView helpingHandsCounter = (TextView)view.findViewById(R.id.helpingHandsCounter);
                TextView state = (TextView)view.findViewById(R.id.state);
                try {
                    json_data = mRequestsArray.getJSONObject(i);
                    patient = setPatient(json_data, new Patient());
                    name.setText(patient.getName());
                    bloodGroup.setText(patient.getBloodGroup());
                    city.setText(patient.getCity());
                    state.setText(patient.getState());
                    if(patient.getGender().equals("Male")){
                        memberImg.setImageResource(R.drawable.male);
                    } else {
                        memberImg.setImageResource(R.drawable.female);
                    }
                    if(patient.getHelpingUsers().length()>1){
                        helpingHandsCounter.setText("+3");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRequestContainer.addView(view, i);
            }
        }
    }

    private JSONArray searchResultInJson(JSONObject json) {
        JSONArray requiredJson = new JSONArray();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).getString("userId").equals(userProfileManager.getUserId())){
                    // Check date
                    Date currentDate = null;
                    Date lastDateOfNeed = null;
                    SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        currentDate = mdformat.parse(getCurrentDate());
                        lastDateOfNeed = mdformat.parse(json.getJSONObject(key).getString("date"));
                        if(lastDateOfNeed.after(currentDate)||lastDateOfNeed.equals(currentDate)){
                            requiredJson.put(json.getJSONObject(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requiredJson;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
        progress.dismiss();
    }

    @Override
    protected String getTitle() {
        return Constants.kHomeFragment;
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true;
        }
        toast.cancel();
        mActivity.finish();
        return true;
    }
}

