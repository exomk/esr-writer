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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.widget.TextView;


public class ESRWriterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String cardId;
        String cardHolder;
        SharedPreferences sharedPreferences;
        TextView carIdTextView;
        TextView cardHolderTextView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esr_writer);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cardId = sharedPreferences.getString(SetupActivity.CARD_ID, "");
        cardHolder = sharedPreferences.getString(SetupActivity.CARD_HOLDER, "");

        carIdTextView = (TextView) findViewById(R.id.cardIdTextView);
        cardHolderTextView = (TextView) findViewById(R.id.cardHolderTextView);
        carIdTextView.setText(cardId);
        cardHolderTextView.setText(cardHolder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();

        return super.onTouchEvent(event);
    }
}
