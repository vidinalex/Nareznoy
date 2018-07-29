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

import com.example.vidinalex.helpme.helpers.PhoneLinker;
import com.example.vidinalex.helpme.R;

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
                //TODO вставить проверку на правильность телефона(новый класс из метода проверки по маскам) и если телефон уже есть или телефон зареган на другого чела
                PhoneLinker.linkPhoneToAccount(etPhone.getText().toString().trim(), PhoneEnterActivity.this);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
