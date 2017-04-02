package com.majavrella.bloodfactory.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.appbase.BaseFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.register.RegisterConstants;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 3/25/2017.
 */

public class ForgotPassword extends DialogFragment {

    private static View mForgotPassword;
    private String mobile;
    @Bind(R.id.user_mob) EditText mUserMobile;
    @Bind(R.id.get_my_password) Button mGetMyPassword;
    private static String userPassword = null;
    protected ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mForgotPassword = inflater.inflate(R.layout.fragment_forgot_password, container);
        ButterKnife.bind(this, mForgotPassword);
        progress=new ProgressDialog(getActivity());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new Digits.Builder().build());
        mUserMobile.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mGetMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                progress.setMessage(RegisterConstants.waitProgress);
                progress.show();
                mobile = mUserMobile.getText().toString().trim();
                if(mobile.matches(Constants.mobRegex)){
                    getJsonData(Constants.kBaseUrl+Constants.kUserList);
                }else{
                    progress.dismiss();
                    mUserMobile.setError(Constants.mobErrorText);
                }
            }
        });
        return mForgotPassword;
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) getActivity()).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            if(!phoneNumber.equals(RegisterConstants.countryCode+mobile)){
                showErrorDialog(RegisterConstants.phoneErrorTitle, RegisterConstants.phoneErrorText2);
            } else {
                successDialog();
            }
        }

        @Override
        public void failure(DigitsException error) {
            showErrorDialog(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
        }
    };

    private void showErrorDialog(String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.error);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void successDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("My Password");
        alertDialogBuilder.setIcon(R.drawable.success);
        alertDialogBuilder.setMessage("Successfully verified. Your password is ["+userPassword+"]. can change password after login only.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) { dismiss(); }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getJsonData(final String url) {
        progress.dismiss();
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        verifyUser(json);
                        break;
                    case API_FAIL:
                        showErrorDialog(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
                        break;
                    case API_NETWORK_FAIL:
                        showErrorDialog(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        break;
                    default : {
                    }
                }
            }
        });
    }

    private void authenticateUser(final String mobile) {
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder().withAuthCallBack(authCallback).withPhoneNumber(RegisterConstants.countryCode+mobile);
        Digits.authenticate(authConfigBuilder.build());
    }

    private void verifyUser(JSONObject result) {
        boolean isUserRegistered = false;
        Iterator iterator = result.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(result.getJSONObject(key).get(Constants.kMobileString).toString().equals(mobile)){
                    isUserRegistered = true;
                    userPassword = result.getJSONObject(key).get(Constants.kPasswordString).toString();
                    authenticateUser(mobile);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!isUserRegistered){
            showErrorDialog(RegisterConstants.registrationErrorTitle, RegisterConstants.registerErrorText);
        }
    }
}
