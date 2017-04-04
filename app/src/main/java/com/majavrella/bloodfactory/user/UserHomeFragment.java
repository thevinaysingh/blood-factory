package com.majavrella.bloodfactory.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.BackButtonSupportFragment;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHomeFragment extends UserFragment implements BackButtonSupportFragment {

    private static View userHomeFragment;
    private boolean consumingBackPress = true;
    private Toast toast;

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
        fetchDataFromCloud();
        super.onResume();
    }

    private void fetchDataFromCloud() {
        String url = Constants.kBaseUrl+Constants.kUsers+"-Kgjp6M1hCcV7A_y1iYM.json";
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                Log.d("User data", "resultWithJSON: "+json);
            }
        });
    }

    @Override
    protected String getTitle() {
        return Constants.kHomeFragment;
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back again to exit !!!", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true;
        }
        toast.cancel();
        return false;
    }

}

