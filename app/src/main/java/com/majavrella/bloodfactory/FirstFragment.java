package com.majavrella.bloodfactory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.majavrella.bloodfactory.base.BaseFragment;

public class FirstFragment extends BaseFragment {

    private View firstFragmentView;
    private Button loginBtn,registerBtn;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstFragmentView = inflater.inflate(R.layout.main_fragment, container, false);
        loginBtn = (Button)firstFragmentView.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninFragment loginFragment = new SigninFragment();
                mFragmentTransaction.replace(R.id.front_fragment_container, loginFragment).addToBackStack(null).commit();

            }
        });
        registerBtn = (Button)firstFragmentView.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment recieveFragment = new RegisterFragment();
                mFragmentTransaction.replace(R.id.front_fragment_container, recieveFragment).addToBackStack(null).commit();

            }
        });
        return firstFragmentView;
    }

}
