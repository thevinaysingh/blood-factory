package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersionFragment extends UserFragment {
    private static View mVersionFragment;

    public static VersionFragment newInstance() {
        return new VersionFragment();
    }

    public VersionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mVersionFragment = inflater.inflate(R.layout.fragment_version, container, false);
        ButterKnife.bind(this, mVersionFragment);
        return mVersionFragment;
    }

    @Override
    protected String getTitle() {
        return "Version";
    }
}
