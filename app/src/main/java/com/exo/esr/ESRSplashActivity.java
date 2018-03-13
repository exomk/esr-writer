package com.exo.esr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ESRSplashActivity extends AppCompatActivity {

    public static String CARD_HOLDER = "user";
    public static String CARD_ID = "card_id";
    private static int SPLASH_TIME_OUT = 3000;

    private SharedPreferences sharedPreferences;

    private ESRSplashActivity esrSplashActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        esrSplashActivity = this;

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent intent;

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(esrSplashActivity);
                if (sharedPreferences.getString(CARD_HOLDER, null) == null) {
                    intent = new Intent(esrSplashActivity, ESRSetupActivity.class);
                } else {
                    intent = new Intent(esrSplashActivity, ESRRegisterActivity.class);
                }
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
