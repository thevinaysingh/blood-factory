package com.majavrella.bloodinformer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
import com.majavrella.bloodinformer.appbase.FragmentCallback;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.register.RegisterConstants;
import com.msg91.sendotp.library.Verification;
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

public class VerifyPin extends DialogFragment {

    private static View mVerifyPinView;
    private static Verification mVerification;
    private String pinNo;
    @Bind(R.id.pin_no) EditText mPinNo;
    @Bind(R.id.resend)
    TextView mResend;
    @Bind(R.id.verify_pin) Button mVerifyPin;
    protected ProgressDialog progress;

    public void setVerificationListener(Verification mVerification) {
        this.mVerification = mVerification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mVerifyPinView = inflater.inflate(R.layout.fragment_verify_pin, container);
        ButterKnife.bind(this, mVerifyPinView);
        progress=new ProgressDialog(getActivity());
        mPinNo.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mVerifyPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                progress.setMessage(RegisterConstants.waitProgress);
                progress.show();
                pinNo = mPinNo.getText().toString().trim();
                if(pinNo.matches(Constants.pinRegex)){
                    mVerification.verify(pinNo);
                    progress.dismiss();
                }else{
                    progress.dismiss();
                    mPinNo.setError(Constants.pinErrorText);
                }
            }
        });

        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerification.resend("voice");
            }
        });
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                mResend.setText("voice call in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mResend.setClickable(true);
                mResend.setText("voice call");
            }
        }.start();
        return mVerifyPinView;
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) getActivity()).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
