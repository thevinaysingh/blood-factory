package com.majavrella.bloodfactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.majavrella.bloodfactory.activities.MainActivity;

public class MainSplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainSplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 500);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}