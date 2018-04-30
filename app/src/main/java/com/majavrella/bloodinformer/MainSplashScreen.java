package com.majavrella.bloodinformer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.majavrella.bloodinformer.appbase.MainActivity;

public class MainSplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.majavrella.bloodinformer.R.layout.main_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainSplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}