package com.majavrella.bloodinformer.user;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserFragment;
import com.majavrella.bloodinformer.register.RegisterConstants;

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

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    View.OnClickListener mChangePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            if(isNetworkAvailable()){
                reset();
                setDataInStringFormat();
                boolean isBothFields = dataValidation();
                if(isBothFields){
                    if(oldPassword.equals(userProfileManager.getPassword())){
                        progress.setMessage(RegisterConstants.waitProgress);
                        progress.show();
                        changePassword();
                    } else {
                       mPasswordError.setText("Old password is not correct");
                    }
                } else {
                    mPasswordError.setText("Password should be more than 6 chars");
                }

            } else {
                showSnackbar(mChangePasswordView, RegisterConstants.networkErrorText);
            }
        }
    };

    private void changePassword() {
        final FirebaseUser user = mFirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(userProfileManager.getMobile()+RegisterConstants.userIdDummyTail, oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassowrd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference mDonarListDatabase = getRootReference().child(RegisterConstants.user_list_db);
                                        mDonarListDatabase.child(userProfileManager.getUserListSelfRefKey()).child("password").setValue(newPassowrd);
                                        userProfileManager.setPassword(newPassowrd);
                                        showSuccessDialog();
                                        progress.dismiss();
                                    } else {
                                        progress.dismiss();
                                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                                    }
                                }
                            });
                        } else {
                            progress.dismiss();
                            Toast.makeText(mActivity, "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You have successfully updated your password")
                .setTitle("Updation success")
                .setIcon(R.drawable.success)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPasswordError.setTextColor(Color.parseColor("#209a85"));
                        mPasswordError.setText("Updated");
                        mOldPassword.setText("");
                        mNewPassword.setText("");
                        Toast.makeText(mActivity, "done", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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
