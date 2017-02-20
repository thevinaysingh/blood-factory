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
public class BloodRequestFragment extends UserFragment {

    public static BloodRequestFragment newInstance() {
        return new BloodRequestFragment();
    }



    public BloodRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_request, container, false);
    }

    @Override
    protected String getTitle() {
        return "Post Blood request";
    }
}
