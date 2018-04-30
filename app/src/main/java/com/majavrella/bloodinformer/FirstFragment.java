package com.majavrella.bloodinformer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.majavrella.bloodinformer.appbase.BaseFragment;
import com.majavrella.bloodinformer.base.BackButtonSupportFragment;
import com.majavrella.bloodinformer.base.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FirstFragment extends BaseFragment implements BackButtonSupportFragment {

    private static View mFirstFragment;
    @Bind(com.majavrella.bloodinformer.R.id.loginBtn) Button mloginBtn;
    @Bind(com.majavrella.bloodinformer.R.id.registerBtn) Button mRegisterBtn;
    private boolean consumingBackPress = true;
    private Toast toast;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirstFragment = inflater.inflate(com.majavrella.bloodinformer.R.layout.main_fragment, container, false);
        ButterKnife.bind(this, mFirstFragment);
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(SigninFragment.newInstance());
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               add(RegisterFragment.newInstance());
            }
        });
        setStatusBarColor(Constants.colorGrey);
        return mFirstFragment;
    }


    @Override
    protected String getTitle() {
        return Constants.kFirstFragment;
    }

    @Override
    public void onResume() {
        hideKeyboard(mActivity);
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true;
        } else {
            toast.cancel();
            mActivity.finish();
            return true;
        }
    }
}
