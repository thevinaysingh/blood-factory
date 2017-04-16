package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.BackButtonSupportFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHomeFragment extends UserFragment implements BackButtonSupportFragment {

    private static View userHomeFragment;
    private boolean consumingBackPress = true;
    private final String TAG = "UserHomeFragment";
    private Toast toast;
    protected SharedPreferences mSharedpreferences;
    @Bind(R.id.donate_blood) LinearLayout mDonateButton;
    @Bind(R.id.find_blood) LinearLayout mFindButton;

    public static UserHomeFragment newInstance() {
        return new UserHomeFragment();
    }

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userHomeFragment = inflater.inflate(R.layout.fragment_user_home, container, false);
        ButterKnife.bind(this, userHomeFragment);

        mSharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        setStatusBarColor(Constants.colorStatusBar);
        mDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(DonateFragment.newInstance());
            }
        });
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(RecieveFragment.newInstance());
            }
        });

        return userHomeFragment;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kHomeFragment;
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true;
        }
        toast.cancel();
        mActivity.finish();
        return true;
    }
}

