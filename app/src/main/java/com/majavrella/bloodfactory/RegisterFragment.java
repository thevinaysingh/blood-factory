package com.majavrella.bloodfactory;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.majavrella.bloodfactory.appbase.BaseFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.modal.RegisterUser;
import com.majavrella.bloodfactory.modal.UserData;
import com.majavrella.bloodfactory.register.RegisterConstants;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class RegisterFragment extends BaseFragment {

    private static View mRegisterFragment;
    private String name, mobile, password;
    private DatabaseReference mDatabase;
    @Bind(R.id.user_name) EditText mUsername;
    @Bind(R.id.user_id) EditText mUserId;
    @Bind(R.id.user_password) EditText mUserPassword;
    @Bind(R.id.show_password) CheckBox mShowPassword;
    @Bind(R.id.register) Button mRegister;
    @Bind(R.id.text_signup) TextView mTextSignup;
    @Bind(R.id.back) FrameLayout mBack;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRegisterFragment = inflater.inflate(R.layout.register_fragment, container, false);
        ButterKnife.bind(this, mRegisterFragment);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(mActivity, new TwitterCore(authConfig), new Digits.Builder().build());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(FirstFragment.newInstance());}
        });
        mRegister.setOnClickListener(mRegisterButtonListener);
        mTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(SigninFragment.newInstance()); }
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
        setStatusBarColor(Constants.colorLogin);
        return mRegisterFragment;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(!isNameValid(name)){
            validation = false;
            mUsername.setError(Constants.nameErrorText);
        }
        if(!isPhoneValid(mobile)){
            validation = false;
            mUserId.setError(Constants.mobErrorText);
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

    private RegisterUser setDataInModal(RegisterUser registerUser) {
        registerUser.setMobile(mobile);
        registerUser.setPassword(password);
        return registerUser;
    }

    private UserData setUserDataInModal(UserData user) {
        user.setName(capitalizeFirstLetter(name));
        return user;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mUsername);
        mobile = getStringDataFromEditText(mUserId);
        password = getStringDataFromEditText(mUserPassword);
    }

    private void resetData() {
        name = mobile = password = null;
    }

    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            if(!phoneNumber.equals(RegisterConstants.countryCode+mobile)){
                showDialogError(RegisterConstants.phoneErrorTitle,RegisterConstants.phoneErrorText2 );
                return;
            }

            progress.setMessage(RegisterConstants.registrationProgress);
            progress.setCancelable(false);
            progress.show();
            final  String user_id = mobile+RegisterConstants.userIdDummyTail;
            mFirebaseAuth.createUserWithEmailAndPassword(user_id, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        try{
                            setDataOnCloud(user.getUid().toString());
                            progress.dismiss();
                            showRegistrationSuccessDialog();
                        } catch(Exception e){
                            e.printStackTrace();
                            progress.dismiss();
                            user.delete();
                            showDialogError(RegisterConstants.registrationErrorTitle, RegisterConstants.registrationErrorText);
                        }
                    } else {
                        progress.dismiss();
                        showDialogError(RegisterConstants.registrationErrorTitle, RegisterConstants.registrationErrorText);
                    }
                }
            });
        }

        @Override
        public void failure(DigitsException exception) {
            showDialogError(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
        }
    };

    private void setDataOnCloud(String userId) {
        DatabaseReference mUserListDatabase = mDatabase.child(RegisterConstants.user_list_db);
        String temp_key = mUserListDatabase.push().getKey();

        DatabaseReference mUserDataDatabase = mDatabase.child(RegisterConstants.user_Data_db);
        String ref_key = mUserDataDatabase.push().getKey();

        RegisterUser registerUser = setDataInModal(new RegisterUser());
        registerUser.setUserId(userId);
        registerUser.setRefKey(ref_key);
        registerUser.setSelfRefKey(temp_key);

        UserData userData = setUserDataInModal(new UserData());
        userData.setUserId(userId);
        userData.setRefKey(temp_key);
        userData.setSelfRefKey(ref_key);

        mUserListDatabase.child(temp_key).setValue(registerUser);
        mUserDataDatabase.child(ref_key).setValue(userData);
    }

    private void showRegistrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(R.string.register_success_msg)
                .setTitle(R.string.register_success_title)
                .setIcon(R.drawable.success)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add(SigninFragment.newInstance());
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkIfUserExist(final String user_id) {
        progress.setMessage(RegisterConstants.verificationProgress);
        progress.setCancelable(false);
        progress.show();
        mFirebaseAuth.fetchProvidersForEmail(user_id).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getProviders().size()>0){
                        progress.dismiss();
                        showDialogError(RegisterConstants.phoneErrorTitle, RegisterConstants.phoneErrorText);
                        return;
                    } else {
                        progress.dismiss();
                        authenticateUser();
                    }
                } else {
                    progress.dismiss();
                    showDialogError(RegisterConstants.verificationErrorTitle, RegisterConstants.verificationErrorText);
                }
            }
        });
    }

    private void authenticateUser() {
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder().withAuthCallBack(authCallback).withPhoneNumber(RegisterConstants.countryCode+mobile);
        Digits.authenticate(authConfigBuilder.build());
    }

    View.OnClickListener mRegisterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                resetData(); setDataInStringFormat(); boolean isAllFieldsValid = dataValidation();
                if(isAllFieldsValid){
                    checkIfUserExist(mobile+RegisterConstants.userIdDummyTail);
                }
            } else {
                showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
            }
        }
    };

    protected String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Override
    protected String getTitle() {
        return Constants.kRegisterFragment;
    }
}
