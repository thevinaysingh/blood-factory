package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mForgotPassword = inflater.inflate(R.layout.fragment_forgot_password, container);
        ButterKnife.bind(this, mForgotPassword);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new Digits.Builder().build());
        mUserMobile.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mGetMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mUserMobile.getText().toString().trim();
                boolean isMobileFieldValid = mobile.matches(Constants.mobRegex);
                if(mobile.matches(Constants.mobRegex)){
                    BaseFragment.hideKeyboard(getContext());
                    new FPJsonTask().execute(Constants.kBaseUrl+Constants.kUserList);
                    dismiss();
                }else{
                    mUserMobile.setError(Constants.mobErrorText);
                }
            }
        });
        return mForgotPassword;
    }

    private void authenticateUser(final String mobile) {
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder().withAuthCallBack(authCallback).withPhoneNumber(RegisterConstants.countryCode+mobile);
        Digits.authenticate(authConfigBuilder.build());
    }

    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            successDialog();
            /*if(!phoneNumber.equals(RegisterConstants.countryCode+mobile)){
                showErrorDialog(RegisterConstants.phoneErrorTitle, RegisterConstants.phoneErrorText2);
            } else {
                successDialog();
            }*/
        }

        @Override
        public void failure(DigitsException error) {
            showErrorDialog(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
        }
    };

    private void successDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("My Password");
        alertDialogBuilder.setIcon(R.drawable.success);
        alertDialogBuilder.setMessage("Successfully verified. Your password is ["+userPassword+"]. can change password after login only.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class FPJsonTask extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progress;

        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        protected JSONObject doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());
                return jsonObject;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            progress.dismiss();
            Iterator iterator = result.keys();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                try {
                    if(result.getJSONObject(key).get(Constants.kMobileString).toString().equals(mobile)){
                        userPassword = result.getJSONObject(key).get(Constants.kPasswordString).toString();
                        authenticateUser(mobile);
                    }
                } catch (JSONException e) {
                    dismiss();
                    e.printStackTrace();
                }
            }
            //showErrorDialog(RegisterConstants.phoneErrorTitle, RegisterConstants.registerErrorText);
        }
    }

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
}
