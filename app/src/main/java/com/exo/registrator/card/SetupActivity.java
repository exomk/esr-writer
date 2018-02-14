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

package com.exo.registrator.card;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.exo.registrator.card.R;

public class SetupActivity extends AppCompatActivity {

    public static String CARD_HOLDER = "user";
    public static String CARD_ID = "card_id";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText editCardHolder;
    private EditText editCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString(CARD_HOLDER, null) == null) {
            setContentView(R.layout.activity_setup);
        } else {
            finish();
        }

    }

    protected void setUp(View button) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        editCardHolder = (EditText) findViewById(R.id.editUser);
        editCardId = (EditText) findViewById(R.id.editCardId);

        editor.putString(CARD_HOLDER, editCardHolder.getText().toString());
        editor.putString(CARD_ID, editCardId.getText().toString());
        editor.commit();

        this.finish();
    }
}
