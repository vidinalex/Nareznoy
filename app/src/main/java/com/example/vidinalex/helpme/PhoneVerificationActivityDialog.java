package com.example.vidinalex.helpme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneVerificationActivityDialog extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification_dialog);

        Button bOk = findViewById(R.id.bOk);
        Button bCancel = findViewById(R.id.bCancel);
        final EditText ETConfCode = findViewById(R.id.etConfirmationCode);

        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ETConfCode.getText().toString().length() ==6)
                {
                    Intent intent = new Intent();
                    intent.putExtra("verificationCode", ETConfCode.getText().toString().trim());
                    intent.setAction("verificationCodeGot");
                    PhoneVerificationActivityDialog.this.sendBroadcast(intent);
                    PhoneVerificationActivityDialog.this.finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Код введён неверно", Toast.LENGTH_SHORT).show();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneVerificationActivityDialog.this.finish();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
