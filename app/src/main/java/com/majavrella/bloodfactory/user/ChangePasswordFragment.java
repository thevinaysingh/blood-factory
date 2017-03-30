package com.majavrella.bloodfactory.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePasswordFragment extends UserFragment {

    private static View mChangePasswordView;
    private String oldPassword, newPassowrd;

    @Bind(R.id.password_error) TextView mPasswordError;
    @Bind(R.id.old_password) EditText mOldPassword;
    @Bind(R.id.new_password) EditText mNewPassword;
    @Bind(R.id.change_password) Button mChangePassword;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChangePasswordView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, mChangePasswordView);

        setStatusBarColor(Constants.colorStatusBarSecondary);
        mChangePassword.setOnClickListener(mChangePasswordListener);
        return mChangePasswordView;
    }

    View.OnClickListener mChangePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reset();
            setDataInStringFormat();
            boolean isBothFields = dataValidation();
            if(isBothFields == false){
                mPasswordError.setText("Something went wrong!");
            } else {
                if(oldPassword.equals(newPassowrd)){
                    mPasswordError.setTextColor(Color.parseColor("#228327"));
                    mPasswordError.setText("Password is successfully updated!!!");
                }else{
                    mPasswordError.setText("Got difference between passwords!");
                }
            }
        }
    };

    private boolean dataValidation() {
        boolean validation = true;
        if(oldPassword.equals("")||oldPassword == null||oldPassword.length()<6){
            mOldPassword.setError(Constants.commonErrorText);
            validation = false;
        }
        if (newPassowrd.equals("")||newPassowrd == null||newPassowrd.length()<6){
            mNewPassword.setError(Constants.commonErrorText);
            validation = false;
        }

        return validation;
    }

    private void setDataInStringFormat() {
        oldPassword = getStringDataFromEditText(mOldPassword);
        newPassowrd = getStringDataFromEditText(mNewPassword);
    }

    private void reset() {
        oldPassword = newPassowrd = null;
    }

    @Override
    protected String getTitle() {
        return Constants.kChangePasswordFragment;
    }
}
