package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExtraSettingsFragment extends UserFragment {

    public static ExtraSettingsFragment newInstance() {
        return new ExtraSettingsFragment();
    }



    public ExtraSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extra_settings, container, false);
    }

    @Override
    protected String getTitle() {
        return "Extra Settings";
    }
}
