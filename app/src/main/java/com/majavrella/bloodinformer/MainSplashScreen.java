package com.majavrella.bloodinformer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.digits.sdk.android.Digits;
import com.majavrella.bloodinformer.appbase.MainActivity;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class MainSplashScreen extends Activity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "OcDNJGwlUg1XtfdGjmv4riYAC";
//    private static final String TWITTER_SECRET = "KiCE5JBOsqkgOew9T9sCIfsFIs1w4A6MizjUtC4BTcU5raOiHT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
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