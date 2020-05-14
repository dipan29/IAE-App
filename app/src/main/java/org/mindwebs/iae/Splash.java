package org.mindwebs.iae;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=3000;
    private static final String SHARED_PREFS = "iaeSharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                boolean idSet = sharedPreferences.getBoolean("idSet", false);
                boolean otpSet = sharedPreferences.getBoolean("otpSet", false);

                if(otpSet){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }
                else if(idSet){
                    startActivity(new Intent(Splash.this, Otp.class));
                } else {
                    Intent i = new Intent(Splash.this,
                            Register.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
