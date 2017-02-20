package com.majavrella.bloodfactory;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.majavrella.bloodfactory.activities.MainActivity;
import com.majavrella.bloodfactory.base.BaseFragment;

public class SigninFragment extends BaseFragment {

    private View signinFragment;
    private EditText userName, userPassword;
    private Button signin;
    private TextView textRegister, lostPassword, emailError, passwordError;
    private FrameLayout back;
    private FirstFragment firstFragment;
    private RegisterFragment registerFragment;
    private CheckBox showPassword;

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signinFragment = inflater.inflate(R.layout.login_fragment, container, false);
        firstFragment = new FirstFragment();
        registerFragment = new RegisterFragment();

        userName = (EditText) signinFragment.findViewById(R.id.userEmail);
        userPassword = (EditText) signinFragment.findViewById(R.id.userPassword);
        showPassword = (CheckBox)signinFragment.findViewById(R.id.showPassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        emailError = (TextView) signinFragment.findViewById(R.id.emailError);
        passwordError = (TextView) signinFragment.findViewById(R.id.passwordError);
        back = (FrameLayout) signinFragment.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction.replace(R.id.front_fragment_container, firstFragment).commit();
            }
        });

        lostPassword = (TextView) signinFragment.findViewById(R.id.lostPassword);
        lostPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Lost password cliked!!", Toast.LENGTH_SHORT).show();
            }
        });

        signin = (Button) signinFragment.findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if (isValid){
                    progress.setMessage("Validating user...");
                    progress.show();
                    String email = userName.getText().toString();
                    String password = userPassword.getText().toString();
                    email = email.trim();
                    password = password.trim();
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progress.dismiss();
                                        Intent intent = new Intent(mActivity, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(mActivity, " Successfully login!!!", Toast.LENGTH_SHORT).show();
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
                } else {
                    progress.dismiss();
                    Toast.makeText(mActivity, "Validation Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textRegister = (TextView) signinFragment.findViewById(R.id.textRegister);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction.replace(R.id.front_fragment_container, registerFragment).commit();
            }
        });

        return signinFragment;
    }

    private boolean dataValidation() {
        boolean validation = false;
        if(userName.getText().toString().equals("")){
            validation = false;
            emailError.setText("Enter your email please");
            passwordError.setText("");
        } else if(userPassword.getText().toString().equals("")){
            validation = false;
            passwordError.setText("Password not set");
            emailError.setText("");
        } else if(!userName.getText().toString().contains("@")){
            validation = false;
            emailError.setText("Enter email in good format");
            passwordError.setText("");
        } else if(userPassword.getText().toString().length()<6){
            validation = false;
            passwordError.setText("Enter password in 6 letters");
            emailError.setText("");
        } else{
            emailError.setText("");
            passwordError.setText("");
            validation = true;
        }
        return validation;
    }
}
