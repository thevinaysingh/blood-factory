package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecieveFragment extends UserFragment {

    View directRecieveFragment;
    Button directSearchBtn;
    Spinner needBloodGroup, needState, needCity;

    public static RecieveFragment newInstance() {
        return new RecieveFragment();
    }


    public RecieveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        directRecieveFragment = inflater.inflate(R.layout.fragment_recieve, container, false);
        directSearchBtn = (Button) directRecieveFragment.findViewById(R.id.directSearchBtn);
        directSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = dataValidation();
                if(isValid){
                    DonarList donarList = new DonarList();
                    if(donarList!=null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frag_container, donarList).addToBackStack(null).commit();
                    }
                    Toast.makeText(mActivity, "Searching ... !!!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(mActivity, "Please fill all data field !!!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return directRecieveFragment;
    }

    private boolean dataValidation() {
        boolean validation = false;
        if(needBloodGroup.getSelectedItem().toString().equals("--Select blood group--")){
            validation = false;
        } else if(needCity.getSelectedItem().toString().equals("--select city--")){
            validation = false;
        } else if(needState.getSelectedItem().toString().equals("--select state--")){
            validation = false;
        } else{
            validation = true;
        }

        return validation;
    }

    @Override
    protected String getTitle() {
        return "Search Blood";
    }
}
