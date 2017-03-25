package com.majavrella.bloodfactory;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majavrella.bloodfactory.appbase.BaseFragment;
import com.majavrella.bloodfactory.appbase.MainActivity;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.register.RegisterConstants;
import com.majavrella.bloodfactory.user.ForgotPassword;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SigninFragment extends BaseFragment {

    private static View mSigninFragment;
    private String mobile, password;
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

    @Override
    public void onResume() {
        hideKeyboard(mActivity);
        super.onResume();
    }

    View.OnClickListener mLostPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                FragmentManager manager = getActivity().getSupportFragmentManager();
                ForgotPassword forgotPassword = new ForgotPassword();
                forgotPassword.show(manager, "password_layout");
            } else {
                showDialogError(RegisterConstants.networkErrorTitle,RegisterConstants.networkErrorText);
            }
        }
    };


    private void getUserList(String kUserList) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Lost password ?");
        alertDialogBuilder.setIcon(R.drawable.error);
        alertDialogBuilder.setMessage("Enter your mobile no and select get my password");

        alertDialogBuilder.setPositiveButton("Get my password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startUserActivity() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(mActivity, " Successfully login!!!", Toast.LENGTH_SHORT).show();
    }

    private void setDataInStringFormat() {
        mobile = getStringDataFromEditText(mUserMobile);
        password = getStringDataFromEditText(mUserPassword);
    }

    private void resetData() {
        mobile = password = null;
    }

    View.OnClickListener mSigninListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                resetData(); setDataInStringFormat(); boolean isAllFieldsValid = dataValidation();
                if (isAllFieldsValid){
                    progress.setMessage(RegisterConstants.validationProgress); progress.show();
                    final  String user_id = mobile+RegisterConstants.userIdDummyTail;
                    mFirebaseAuth.signInWithEmailAndPassword(user_id, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    startUserActivity();
                                } else {
                                    progress.dismiss();
                                    showDialogError(RegisterConstants.loginErrorTitle,RegisterConstants.loginErrorText);
                                }
                            }
                        });
                }
            } else {
                showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
            }

        }
    };

    @Override
    protected String getTitle() {
        return Constants.kLoginFragment;
    }
}
