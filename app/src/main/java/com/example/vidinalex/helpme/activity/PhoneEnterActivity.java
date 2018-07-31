package com.example.vidinalex.helpme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.helpers.PhoneLinker;
import com.example.vidinalex.helpme.helpers.RegisterDataFilter;

public class PhoneEnterActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_enter);

        final Button bGetNum = findViewById(R.id.bRegister);
        final EditText etPhone = findViewById(R.id.etPhoneNumber);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(broadcastReceiver);
                finish();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("PhoneConfirmed"));

        bGetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RegisterDataFilter.checkPhone(PhoneEnterActivity.this, etPhone.getText().toString().trim()))
                    PhoneLinker.linkPhoneToAccount(etPhone.getText().toString().trim(), PhoneEnterActivity.this);
                else
                    Toast.makeText(PhoneEnterActivity.this, "телефон не по формату", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
