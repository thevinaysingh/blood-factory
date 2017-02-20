package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

public class AddMemberFragment extends UserFragment {

    private View view;

    public static AddMemberFragment newInstance() {
        return new AddMemberFragment();
    }

    public AddMemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_member, container, false);


        return view;
    }

    @Override
    protected String getTitle() {
        return "Add Member";
    }

}
