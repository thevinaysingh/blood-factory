package com.majavrella.bloodinformer.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.majavrella.bloodinformer.FirstFragment;
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.VerifyPin;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
import com.majavrella.bloodinformer.appbase.BaseFragment;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.register.RegisterConstants;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 3/25/2017.
 */

public class ForgotPassword extends BaseFragment implements VerificationListener{

    private static View mForgotPassword;
    private static VerifyPin verifyPin = new VerifyPin();
    private String mobile;
    @Bind(R.id.user_mob) EditText mUserMobile;
    @Bind(R.id.get_my_password) Button mGetMyPassword;
    @Bind(com.majavrella.bloodinformer.R.id.back)
    FrameLayout mBack;
    private static String userPassword = null;
    protected ProgressDialog progress;
    private static Verification mVerification = new Verification() {
        @Override
        public void initiate() {
            Log.e(">>>>Verification", "mVerification initiate");
        }

        @Override
        public void verify(String s) {
            Log.e(">>>>Verification", "mVerification verify:"+s);
        }

        @Override
        public void resend(String s) {
            Log.e(">>>>Verification", "mVerification resend:"+s);
        }
    };

    public static ForgotPassword newInstance() {
        return new ForgotPassword();
    }

    public ForgotPassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mForgotPassword = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, mForgotPassword);
        progress=new ProgressDialog(getActivity());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new Digits.Builder().build());
        mUserMobile.requestFocus();
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
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(FirstFragment.newInstance());}
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

    private void showErrorDialog(String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.error);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

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
            public void onClick(DialogInterface arg0, int arg1) {  }
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
        if(isNetworkAvailable()){
            mVerification = SendOtpVerification
                    .createSmsVerification(SendOtpVerification
                            .config(RegisterConstants.countryCode+mobile)
                            .context(getActivity())
                            .autoVerification(true)
                            .build(), this);
            mVerification.initiate();
        } else {
            showSnackbar(RegisterConstants.networkErrorText);
        }
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

    @Override
    public void onInitiated(String s) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        verifyPin.setVerificationListener(mVerification);
        verifyPin.show(manager, "verify_pin_layout");
    }

    @Override
    public void onInitiationFailed(Exception e) {
        Toast.makeText(getActivity(), "Unable to send otp. Please, Check your network and try again!", Toast.LENGTH_LONG);
    }

    @Override
    public void onVerified(String s) {
        verifyPin.dismiss();
        successDialog();
    }

    @Override
    public void onVerificationFailed(Exception e) {
        Toast.makeText(getActivity(), "You have entered wrong pin!", Toast.LENGTH_LONG);
    }

    @Override
    protected String getTitle() {
        return Constants.kForgotFragment;
    }
}
