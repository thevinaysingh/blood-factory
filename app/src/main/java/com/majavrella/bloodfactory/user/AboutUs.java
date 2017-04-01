package com.majavrella.bloodfactory.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.ButterKnife;

public class AboutUs extends UserFragment {

    private static View mAboutUsView;

    public static AboutUs newInstance() {
        return new AboutUs();
    }

    public AboutUs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAboutUsView = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, mAboutUsView);

        setStatusBarColor(Constants.colorStatusBar);
        return mAboutUsView;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kGuidanceFragment;
    }
}
