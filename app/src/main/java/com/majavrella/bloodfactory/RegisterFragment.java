package com.majavrella.bloodfactory;


import android.content.DialogInterface;
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
import com.majavrella.bloodfactory.base.BaseFragment;

public class RegisterFragment extends BaseFragment {

    private View registerFragment;
    private EditText username, useremail, userpassword;
    private Button register;
    private TextView textSignup, nameError, emailError, passwordError;
    private FrameLayout back;
    private FirstFragment firstFragment;
    private SigninFragment signinFragment;
    private CheckBox showPassword;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerFragment = inflater.inflate(R.layout.register_fragment, container, false);
        firstFragment = new FirstFragment();
        signinFragment = new SigninFragment();

        username = (EditText) registerFragment.findViewById(R.id.username);
        useremail = (EditText) registerFragment.findViewById(R.id.useremail);
        userpassword = (EditText) registerFragment.findViewById(R.id.userpassword);
        showPassword = (CheckBox)registerFragment.findViewById(R.id.showPassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    userpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        nameError = (TextView) registerFragment.findViewById(R.id.nameError);
        emailError = (TextView) registerFragment.findViewById(R.id.emailError);
        passwordError = (TextView) registerFragment.findViewById(R.id.passwordError);

        back = (FrameLayout) registerFragment.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mFragmentTransaction.replace(R.id.front_fragment_container, firstFragment).commit();
            }
        });
        register = (Button) registerFragment.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if (isValid){
                    progress.setMessage("Registering user...");
                    progress.show();
                    final String name = username.getText().toString().trim();
                    final String email = useremail.getText().toString().trim();
                    final String password = userpassword.getText().toString().trim();
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
        });
        textSignup = (TextView) registerFragment.findViewById(R.id.textSignup);
        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction.replace(R.id.front_fragment_container, signinFragment).commit();
            }
        });
        return registerFragment;
    }

    private boolean dataValidation() {
        boolean validation = false;
        if(username.getText().toString().equals("")){
            validation = false;
            nameError.setText("Enter your name please");
            emailError.setText("");
            passwordError.setText("");
        } else if(useremail.getText().toString().equals("")){
            validation = false;
            emailError.setText("Enter your email please");
            passwordError.setText("");
            nameError.setText("");
        } else if(userpassword.getText().toString().equals("")){
            validation = false;
            passwordError.setText("Password not set");
            emailError.setText("");
            nameError.setText("");
        } else if(!useremail.getText().toString().contains("@")){
            validation = false;
            emailError.setText("Enter email in good format");
            passwordError.setText("");
            nameError.setText("");
        } else if(userpassword.getText().toString().length()<6){
            validation = false;
            passwordError.setText("Enter password in 6 letters");
            emailError.setText("");
            nameError.setText("");
        } else{
            emailError.setText("");
            passwordError.setText("");
            nameError.setText("");
            validation = true;
        }
        return validation;
    }

}
