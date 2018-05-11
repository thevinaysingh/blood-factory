package com.majavrella.bloodinformer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends UserFragment {

    public static EmergencyFragment newInstance() {
        return new EmergencyFragment();
    }

    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    @Override
    protected String getTitle() {
        return Constants.kEmergenyFragment;
    }
}
