package com.majavrella.bloodfactory;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.appbase.BaseFragment;
import com.majavrella.bloodfactory.appbase.MainActivity;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.register.RegisterConstants;
import com.majavrella.bloodfactory.user.ForgotPassword;
import com.majavrella.bloodfactory.user.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SigninFragment extends BaseFragment {

    private static View mSigninFragment;
    private String mobile, password;
    protected SharedPreferences sharedpreferences;
    protected SharedPreferences.Editor editor;
    @Bind(R.id.user_mob) EditText mUserMobile;
    @Bind(R.id.user_password) EditText mUserPassword;
    @Bind(R.id.show_password) CheckBox mShowPassword;
    @Bind(R.id.signin) Button mSignin;
    @Bind(R.id.textRegister) TextView mTextRegister;
    @Bind(R.id.lostPassword) TextView mLostPassword;
    @Bind(R.id.back) FrameLayout mBack;

    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSigninFragment = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, mSigninFragment);

        sharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(FirstFragment.newInstance()); }
        });
        mSignin.setOnClickListener(mSigninListener);
        mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(RegisterFragment.newInstance());}
        });

        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mLostPassword.setOnClickListener(mLostPasswordListener);

        setStatusBarColor(Constants.colorLogin);
        return mSigninFragment;
    }

    private void startUserActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try{
                    Toast.makeText(mActivity, "Login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mActivity, UserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    progress.dismiss();
                }catch (Exception e){
                    progress.dismiss();
                }
            }
        }, 500);


    }

    private boolean dataValidation() {
        boolean validation = true;
        if(!isPhoneValid(mobile)){
            validation = false;
            mUserMobile.setError(Constants.mobErrorText);
        }
        if(password.length()<6){
            validation = false;
            mUserPassword.setError("Enter 6 characters!");
        }
        return validation;
    }

    private void setDataInStringFormat() {
        mobile = getStringDataFromEditText(mUserMobile);
        password = getStringDataFromEditText(mUserPassword);
    }

    private void resetData() {
        mobile = password = null;
    }

    private void setUsersDBRefKeyForCurrentUser() {
        final String url = Constants.kBaseUrl+Constants.kUserList;
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

    private void setUsersDBRefKey(JSONObject json) {
        final String ref_key = extractRefKeyForUsers(json);
        editor.putString(RegisterConstants.usersDataRefKey, ref_key);
        editor.commit();
    }

    private void setUserListDBRefKeyForCurrentUser() {
        final String url = Constants.kBaseUrl+Constants.kUsersData;
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
        final String ref_key = extractRefKeyForUsersList(json);
        editor.putString(RegisterConstants.userListRefKey, ref_key);
        editor.commit();
    }

    private String extractRefKeyForUsersList(JSONObject json) {
        String ref_key = null;
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
                    editor.putString(RegisterConstants.userData, json.getJSONObject(key).toString());
                    editor.commit();
                    ref_key = json.getJSONObject(key).get(Constants.kRefKey).toString();
                    return ref_key;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ref_key;
    }

    private String extractRefKeyForUsers(JSONObject json) {
        String ref_key = null;
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
                    editor.putString(RegisterConstants.usersListData, json.getJSONObject(key).toString());
                    editor.commit();
                    ref_key = json.getJSONObject(key).get(Constants.kRefKey).toString();
                    return ref_key;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ref_key;
    }

    public void showSnackbar(String text) {
        final Snackbar snackbar = Snackbar.make(mSigninFragment, text, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  }
                });
        snackbar.show();
    }

    View.OnClickListener mSigninListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetData(); setDataInStringFormat(); boolean isAllFieldsValid = dataValidation();
                if (isAllFieldsValid){
                    progress.setMessage(RegisterConstants.validationProgress);
                    progress.setCancelable(false);
                    progress.show();
                    final  String user_id = mobile+RegisterConstants.userIdDummyTail;
                    mFirebaseAuth.signInWithEmailAndPassword(user_id, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setUsersDBRefKeyForCurrentUser();
                                setUserListDBRefKeyForCurrentUser();
                                startUserActivity();
                            } else {
                                showLoginFailedSnackbar();
                                showDialogError(RegisterConstants.loginErrorTitle,RegisterConstants.loginErrorText);
                            }
                            progress.dismiss();
                        }
                    });
                }
            } else {
                showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
            }

        }
    };

    private void showLoginFailedSnackbar() {
        final Snackbar snackbar = Snackbar.make(mSigninFragment, "Go to register page", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add(RegisterFragment.newInstance());
                    }
                });
        snackbar.show();
    }

    View.OnClickListener mLostPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                FragmentManager manager = getActivity().getSupportFragmentManager();
                ForgotPassword forgotPassword = new ForgotPassword();
                forgotPassword.show(manager, "password_layout");
            } else {
                showSnackbar(RegisterConstants.networkErrorText);
            }
        }
    };

    @Override
    public void onResume() {
        hideKeyboard(mActivity);
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kLoginFragment;
    }
}
