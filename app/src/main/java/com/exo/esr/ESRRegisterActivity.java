package com.exo.esr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class ESRRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ToggleButton btnRegisterIn = null;
    private ToggleButton btnRegisterBreak = null;
    private ToggleButton btnRegisterOut = null;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esr_register);

        btnRegisterIn = (ToggleButton) findViewById(R.id.btn_register_in);
        btnRegisterIn.setOnClickListener(this);

        btnRegisterBreak = (ToggleButton) findViewById(R.id.btn_register_break);
        btnRegisterBreak.setOnClickListener(this);

        btnRegisterOut = (ToggleButton) findViewById(R.id.btn_register_out);
        btnRegisterOut.setOnClickListener(this);

        registerReceiver(broadcastReceiver, new IntentFilter("registration_success"));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_in:
                ESRWriterService.setRegisterType(ERegisterType.REGISTER_IN);
                btnRegisterIn.setChecked(true);
                btnRegisterIn.setBackgroundColor(getResources().getColor(R.color.colorDarkRed));
                btnRegisterBreak.setChecked(false);
                btnRegisterBreak.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnRegisterOut.setChecked(false);
                btnRegisterOut.setBackgroundColor(getResources().getColor(R.color.colorRed));
                break;
            case R.id.btn_register_break:
                ESRWriterService.setRegisterType(ERegisterType.BREAK);
                btnRegisterIn.setChecked(false);
                btnRegisterIn.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnRegisterBreak.setChecked(true);
                btnRegisterBreak.setBackgroundColor(getResources().getColor(R.color.colorDarkRed));
                btnRegisterOut.setChecked(false);
                btnRegisterOut.setBackgroundColor(getResources().getColor(R.color.colorRed));
                break;
            case R.id.btn_register_out:
                ESRWriterService.setRegisterType(ERegisterType.REGISTER_OUT);
                btnRegisterIn.setChecked(false);
                btnRegisterIn.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnRegisterBreak.setChecked(false);
                btnRegisterBreak.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnRegisterOut.setChecked(true);
                btnRegisterOut.setBackgroundColor(getResources().getColor(R.color.colorDarkRed));
                break;
            default:
                ESRWriterService.setRegisterType(ERegisterType.REGISTER_IN);
        }
    }

}
