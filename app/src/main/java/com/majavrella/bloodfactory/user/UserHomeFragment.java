package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONException;
import org.json.JSONObject;

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

        progressDialog.setMessage(RegisterConstants.waitProgress);
        progressDialog.show();
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
    public void onResume() {
        hideKeyboard(getActivity());
        fetchDataFromCloud();
        super.onResume();
    }

    private void fetchDataFromCloud() {
        if(isNetworkAvailable()) {
            setDataFromCloud();
        } else {
            setDataFromLocal();
        }
    }

    private void setDataFromCloud() {
        final String user_list_ref_key = mSharedpreferences.getString(RegisterConstants.userListRefKey, "DEFAULT_VALUE");
        final String user_ref_key = mSharedpreferences.getString(RegisterConstants.usersDataRefKey, "DEFAULT_VALUE");
        if(user_ref_key.equals("DEFAULT_VALUE")&&user_list_ref_key.equals("DEFAULT_VALUE")) {
            getUserDataFromDb();
            Log.d(TAG, "getUserDataFromDirectDb: "+user_list_ref_key+"\n"+ user_ref_key);
        } else {
            Log.d(TAG, "getUserDataFromDirectDb: "+user_list_ref_key+"\n"+ user_ref_key);
            getUserDataFromDirectDb(user_ref_key, user_list_ref_key);
        }
    }

    private void getUserDataFromDirectDb(String user_ref_key, String user_list_ref_key) {
        final String user_list_url = Constants.kBaseUrl+Constants.kUserListUrl+user_list_ref_key+Constants.jsonTail;
        APIManager.getInstance().callApiListener(user_list_url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setDataOnLocalFromUserListDb(json);
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
            }
        });

        final String user_url = Constants.kBaseUrl+Constants.kUsers+user_ref_key+Constants.jsonTail;
        APIManager.getInstance().callApiListener(user_url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setDataOnLocalFromUserDb(json);
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
            }
        });

    }

    private void setDataOnLocalFromUserDb(JSONObject json) {
        Log.d(TAG, "setDataOnLocalFromUserDb: "+json);
    }

    private void setDataOnLocalFromUserListDb(JSONObject json) {
        Log.d(TAG, "setDataOnLocalFromUserListDb: "+json);
    }

    private void getUserDataFromDb() {
        final String user_list_url = Constants.kBaseUrl+Constants.kUserList;
        APIManager.getInstance().callApiListener(user_list_url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setDataFromUserList(json);
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
            }
        });

        final String user_url = Constants.kBaseUrl+Constants.kUsersData;
        APIManager.getInstance().callApiListener(user_url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setDataFromUsers(json);
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
            }
        });
    }

    private void setDataFromUserList(JSONObject json) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
                    Log.d("--------", "setDataFromUserList: "+json.getJSONObject(key));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataFromUsers(JSONObject json) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
                    Log.d("--------", "setDataFromUsers: "+json.getJSONObject(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataFromLocal() {
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

