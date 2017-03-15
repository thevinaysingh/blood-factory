package com.majavrella.bloodfactory.appbase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.majavrella.bloodfactory.base.BackButtonSupportFragment;

public abstract class BaseActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected AddFragmentHandler fragmentHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new AddFragmentHandler(fragmentManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
       // fragmentManager.removeOnBackStackChangedListener(backStackListener);
        fragmentManager = null;
        super.onDestroy();
    }

    protected void add(BaseFragment fragment) {
        fragmentHandler.add(fragment);
    }

    @Override
    public void onBackPressed() {
        if (sendBackPressToFragmentOnTop()) {
            return;
        }
        super.onBackPressed();
    }

    private boolean sendBackPressToFragmentOnTop() {
        BaseFragment fragmentOnTop = fragmentHandler.getCurrentFragment();
        if (fragmentOnTop == null) {
            return false;
        }
        if (!(fragmentOnTop instanceof BackButtonSupportFragment)) {
            return false;
        }
        boolean consumedBackPress = ((BackButtonSupportFragment) fragmentOnTop).onBackPressed();
        return consumedBackPress;
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
