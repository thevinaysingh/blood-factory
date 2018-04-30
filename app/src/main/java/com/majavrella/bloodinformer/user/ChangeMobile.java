package com.majavrella.bloodinformer.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserProfileManager;
import com.majavrella.bloodinformer.register.RegisterConstants;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 4/22/2017.
 */

public class ChangeMobile extends DialogFragment {
    private static View mChangeMobile;
    private String mobile, password, oldMobile;
    private UserProfileManager userProfileManager;
    protected ProgressDialog progress;
    @Bind(R.id.user_mob)
    EditText mUserMobile;
    @Bind(R.id.user_old_mobile)
    EditText mUserOldMobile;
    @Bind(R.id.user_password)
    EditText mUserPassword;
    @Bind(R.id.change_my_mobile)
    Button mChangeMyMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChangeMobile = inflater.inflate(R.layout.change_mobile, container);
        ButterKnife.bind(this, mChangeMobile);
        userProfileManager = UserProfileManager.getInstance();
        progress=new ProgressDialog(getActivity());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new Digits.Builder().build());
        mUserMobile.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mChangeMyMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(setDataInStringFormat()){
                    progress.setMessage(RegisterConstants.waitProgress);
                    progress.show();
                    changeMobileNo();
                }else{
                    Toast.makeText(getActivity(), "Input error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mChangeMobile;
    }

    private boolean setDataInStringFormat() {
        boolean validation = true;
        mobile = oldMobile = password = null;
        mobile = mUserMobile.getText().toString().trim();
        oldMobile = mUserOldMobile.getText().toString().trim();
        password = mUserPassword.getText().toString().trim();
        if(!mobile.matches(Constants.mobRegex)){
            validation = false;
            mUserMobile.setError(Constants.mobErrorText);
        }
        if(!oldMobile.matches(Constants.mobRegex)){
            validation = false;
            mUserOldMobile.setError(Constants.mobErrorText);
        }
        if(password.equals("")||password == null||password.length()<6){
            validation = false;
            mUserPassword.setError("need 6 Chars");
        }
        return validation;
    }

    private void changeMobileNo() {
        APIManager.getInstance().callApiListener(Constants.kBaseUrl+Constants.kUserList, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        if(verifyUser(json)){
                            successDialog("Registered user ", "Mobile no which you are trying to change is already regitered, Try another number");
                        } else {
                            gotoChange();
                        }
                        progress.dismiss();
                        break;
                    case API_FAIL:
                        showErrorDialog(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
                        progress.dismiss();
                        break;
                    case API_NETWORK_FAIL:
                        showErrorDialog(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        progress.dismiss();
                        break;
                    default : {
                    }
                }
            }
        });
    }

    private void gotoChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This mobile no will be verified through OTP and your account will be restored with your existing database.\n\nClick to start processing...")
                .setTitle("Goto change")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authenticateUser(mobile);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                dismiss();
            } else {
                changeMobileNoOnCloud();
            }
        }

        @Override
        public void failure(DigitsException error) {
            showErrorDialog(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
        }
    };

    private void changeMobileNoOnCloud() {
        progress.setMessage(RegisterConstants.waitProgress);
        progress.show();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mFirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(oldMobile+RegisterConstants.userIdDummyTail, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(mobile+RegisterConstants.userIdDummyTail);
                            DatabaseReference mDonarListDatabase = FirebaseDatabase.getInstance().getReference().child(RegisterConstants.user_list_db);
                            mDonarListDatabase.child(userProfileManager.getUserListSelfRefKey()).child("mobile").setValue(mobile);
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Successfully updated your mobile number", Toast.LENGTH_SHORT).show();
                            Intent self = new Intent(getActivity(), UserActivity.class);
                            self.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            self.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(self);
                        } else {
                            progress.dismiss();
                            dismiss();
                            Toast.makeText(getActivity(), "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void successDialog( String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.oops);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) { dismiss(); }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void authenticateUser(final String mobile) {
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder().withAuthCallBack(authCallback).withPhoneNumber(RegisterConstants.countryCode+mobile);
        Digits.authenticate(authConfigBuilder.build());
    }

    private boolean verifyUser(JSONObject result) {
        boolean isUserRegistered = false;
        Iterator iterator = result.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(result.getJSONObject(key).get(Constants.kMobileString).toString().equals(mobile)){
                    isUserRegistered = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isUserRegistered;
    }
}
