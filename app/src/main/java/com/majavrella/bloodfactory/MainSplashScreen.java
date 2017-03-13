package com.majavrella.bloodfactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.majavrella.bloodfactory.activities.MainActivity;

import butterknife.Bind;

public class MainSplashScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
//        ProgressBar mProgressBar = (ProgressBar)findViewById(R.id.fake_progress_bar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainSplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}