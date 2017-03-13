package com.majavrella.bloodfactory;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.majavrella.bloodfactory.activities.BaseFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.modal.RegisterUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class RegisterFragment extends BaseFragment {

    private static View mRegisterFragment;
    private String name, mobile, password;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.kConsumerKey, Constants.kConsumerSecret);
        Fabric.with(mActivity, new TwitterCore(authConfig), new Digits.Builder().build());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(FirstFragment.newInstance());}
        });
        mRegister.setOnClickListener(mRegisterButtonListener);
        mTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { add(SigninFragment.newInstance()); }
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
        registerUser.setName(name);
        registerUser.setMobile(mobile);
        registerUser.setPassword(password);
        return registerUser;
    }

    private void setDataInStringFormat() {
        name = getStringDataFromEditText(mUsername);
        mobile = getStringDataFromEditText(mUserId);
        password = getStringDataFromEditText(mUserPassword);
    }

    private void resetData() {
        name = mobile = password = null;
    }

    /*ValueEventListener checkIfUserExistListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };*/

    /*
    View.OnClickListener mRegisterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isAllFieldsValid = dataValidation();
            if (isAllFieldsValid){
                progress.setMessage("Registering user...");
                progress.show();
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setMessage(R.string.register_success_msg)
                                            .setTitle("Registration successful..thanks")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mFragmentTransaction.replace(R.id.front_fragment_container, signinFragment).commit();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    progress.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setMessage(task.getException().getMessage())
                                            .setTitle(R.string.login_error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });
            }
            else{
                progress.dismiss();
                Toast.makeText(mActivity, "Validation Error!!!", Toast.LENGTH_SHORT).show();
            }

        }
    };
*/
    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
           /* if(!phoneNumber.equals(mobile)){
                return;
            }*/
            progress.setMessage("Registering user...");
            progress.show();
            RegisterUser registerUser = setDataInModal(new RegisterUser());
            mDatabase =  mDatabase.child("user_list");
            mDatabase.setValue(registerUser);
            final  String user_id = mobile+"@bloodfactory.com";
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuth.createUserWithEmailAndPassword(user_id,password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(R.string.register_success_msg)
                                .setTitle("Registration successful..thanks")
                                .setIcon(R.drawable.success)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        add(SigninFragment.newInstance());
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        progress.dismiss();
                        showDialog("Registration Error", "Registration failed, Something went wrong.");
                    }
                }
            });
        }

        @Override
        public void failure(DigitsException exception) {
            showDialog("Token Verification", "You have enterered wrong code!");
        }
    };

    private boolean checkIfUserExist(final String user_id) {
        final boolean[] userExistence = {false};
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.fetchProvidersForEmail(user_id).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if(task.isSuccessful()){
                    userExistence[0] = true;
                    task.getResult().getProviders();
                    return;
                }
            }
        });
        return userExistence[0];
    }

    View.OnClickListener mRegisterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                resetData();
                setDataInStringFormat();
                boolean isAllFieldsValid = dataValidation();
                if(isAllFieldsValid){
                    progress.setMessage("Verifing user...");
                    progress.show();
                    Boolean isUserExist = checkIfUserExist(mobile+"@bloodfactory.com");
                    if(isUserExist) {
                        progress.dismiss();
                        showDialog("Mobile no Error", "This Mobile no is already registered.");
                        return;
                    } else {
                        progress.dismiss();
                        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                                .withAuthCallBack(authCallback)
                                .withPhoneNumber("+91"+mobile);
                        Digits.authenticate(authConfigBuilder.build());
                    }
                }
            } else {
                showDialog("Network Error", "Internet is not available.");
            }
        }
    };

    @Override
    protected String getTitle() {
        return Constants.kRegisterFragment;
    }
}
