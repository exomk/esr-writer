/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2018  EXO Service Solutions
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 * You can contact us at contact4exo@exo.mk
 */

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
    private static int SPLASH_TIME_OUT = 1000;

    private SharedPreferences sharedPreferences;

    private ESRSplashActivity esrSplashActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esr_splash);
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
