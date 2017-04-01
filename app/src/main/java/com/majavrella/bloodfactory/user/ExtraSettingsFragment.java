package com.majavrella.bloodfactory.user;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExtraSettingsFragment extends UserFragment {

    private static View mExtraSettingsView;
    @Bind(R.id.change_mob) LinearLayout mChangeMob;
    @Bind(R.id.edit_profile) LinearLayout mEditProfile;
    @Bind(R.id.change_password) LinearLayout mChangePassword;

    public static ExtraSettingsFragment newInstance() {
        return new ExtraSettingsFragment();
    }

    public ExtraSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mExtraSettingsView = inflater.inflate(R.layout.fragment_extra_settings, container, false);
        ButterKnife.bind(this, mExtraSettingsView);

        mChangeMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                showDialogForChangeMobileNo();
            }
        });
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                add(EditProfileFragment.newInstance());
            }
        });

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                add(ChangePasswordFragment.newInstance());
            }
        });

        return mExtraSettingsView;
    }

    public void showDialogForChangeMobileNo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(Constants.changeMobiletitle);
        builder.setIcon(R.drawable.user_id_lock);
        builder.setMessage(Constants.changeMobileMsg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show(); //show() should be called before dialog.getButton().
        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kExtraSettingsFragment;
    }
}
