package com.majavrella.bloodinformer.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.base.UserFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeveloperFragment extends UserFragment {

    private static View mDeveloperFragment;

    public static DeveloperFragment newInstance() {
        return new DeveloperFragment();
    }

    public DeveloperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDeveloperFragment = inflater.inflate(R.layout.fragment_developer, container, false);
        ButterKnife.bind(this, mDeveloperFragment);
        return mDeveloperFragment;
    }

    @Override
    protected String getTitle() {
        return "Developer";
    }
}
