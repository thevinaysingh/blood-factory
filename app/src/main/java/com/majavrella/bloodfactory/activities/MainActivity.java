package com.majavrella.bloodfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.majavrella.bloodfactory.FirstFragment;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.user.UserActivity;

public class MainActivity extends FragmentActivity {

    static private FirebaseAuth mFirebaseAuth;
    static private FirebaseUser mFirebaseUser;
    static private FragmentManager mFragmentManager;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            loadLogInView();
        }
        else {
            Intent intent = new Intent(this, UserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void loadLogInView() {
        FirstFragment firstFragment = new FirstFragment();
        if(firstFragment!=null){
            mFragmentManager.beginTransaction().add(R.id.front_fragment_container,firstFragment).commit();
        } else {
            // setContentView(R.layout.error_layout);
        }
    }

    @Override
    public void onBackPressed() {
        if(exit){
            finish();
        } else {
            Toast.makeText(this, "Press back again to exit !!!", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit= false;
                }
            },3 * 1000);
        }
    }
}
