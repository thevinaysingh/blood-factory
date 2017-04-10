package com.majavrella.bloodfactory.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.BackButtonSupportFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHomeFragment extends UserFragment implements BackButtonSupportFragment {

    private static View userHomeFragment;
    private boolean consumingBackPress = true;
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

    private void setUsersDBRefKeyForCurrentUser() {
        final String url = Constants.kBaseUrl+Constants.kUsersData;
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setUsersDBRefKey(json);
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

    private void getUserDataFromCloud() {
       /* try {
            progress.setMessage("Logging in ...");
            progress.show();
            Thread.sleep(5000);
            progress.dismiss();
        } catch (Exception e) {
            progress.dismiss();
            e.printStackTrace();
        }*/
        String userListRefKey = getUserListRefkey();
        String usersDataRefKey = getUsersRefkey();
    }

    private void setUsersDBRefKey(JSONObject json) {
        final String ref_key = extractRefKey(json);
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putString(RegisterConstants.usersDataRefKey, ref_key);
        editor.commit();
    }
    private void setUserListDBRefKeyForCurrentUser() {
        final String url = Constants.kBaseUrl+Constants.kUserList;
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setUserListDBRefKey(json);
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

    private void setUserListDBRefKey(JSONObject json) {
        final String ref_key = extractRefKey(json);
        final String url = Constants.kBaseUrl+Constants.kUserList+ref_key+".json";
        getUserDataFromCloud(url);
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putString(RegisterConstants.userListRefKey, ref_key);
        editor.commit();
    }


    private void getUserDataFromCloud(final String url) {
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                Log.d("User List", "resultWithJSON: "+json);
            }
        });
    }

    private String getUserListRefkey() {
        return mSharedpreferences.getString(RegisterConstants.userListRefKey, RegisterConstants.defaultSharedPrefsValue);
    }

    private String getUsersRefkey() {
        return mSharedpreferences.getString(RegisterConstants.usersDataRefKey,RegisterConstants.defaultSharedPrefsValue);
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        //fetchDataFromCloud();
        //getUserDataFromCloud();
        //fetchDataFromCloud();
        super.onResume();
    }

    private void fetchDataFromCloud() {
        try{
            if(mSharedpreferences.getString(RegisterConstants.userListRefKey,"DEFAULT_VALUE").equals("DEFAULT_VALUE")) {
                setUserListDBRefKeyForCurrentUser();
            } else {
                String userListRefKey = mSharedpreferences.getString(RegisterConstants.userListRefKey,"DEFAULT_VALUE");
                final String url = Constants.kBaseUrl+Constants.kUserList+userListRefKey+".json";
                getUserDataFromCloud(url);
            }

            if(mSharedpreferences.getString(RegisterConstants.usersDataRefKey,"DEFAULT_VALUE").equals("DEFAULT_VALUE")) {
                setUsersDBRefKeyForCurrentUser();
            } else {
                final String usersDataRefKey = mSharedpreferences.getString(RegisterConstants.usersDataRefKey,"DEFAULT_VALUE");
                final String url = Constants.kBaseUrl+Constants.kUsers+usersDataRefKey+".json";
                APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
                    @Override
                    public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                        Log.d("User data", "resultWithJSON: "+json);
                    }
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
        }

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
        return false;
    }

}

