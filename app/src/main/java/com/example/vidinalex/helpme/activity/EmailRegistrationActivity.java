package com.example.vidinalex.helpme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.managers.PermissionManager;
import com.example.vidinalex.helpme.utils.PhoneLinker;
import com.example.vidinalex.helpme.utils.RegisterDataFilter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EmailRegistrationActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    private EditText ETemail;
    private EditText ETpassword;
    private EditText ETpasswordConfirmation;
    private EditText ETphone;
    private Button bRegister;
    public String regStage = "Регистрация провалена";
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);

        PermissionManager.checkPermissionsAndRequest(this, PermissionManager.DEFAULT_PERMISSION_PACK);

        mAuth = FirebaseAuth.getInstance();


        ETemail = (EditText) findViewById(R.id.etEmail);
        ETpassword = (EditText) findViewById(R.id.etPassword);
        ETpasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        ETphone = findViewById(R.id.etPhoneNumber);
        bRegister = findViewById(R.id.bRegister);

        Button refToLog = (Button) findViewById(R.id.bRefLogin);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                regStage = "телефон подтверждён, регистрация успешна";
            }
        };


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initFullRegistrationProcess();
            }
        });

        refToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailRegistrationActivity.this, LoginActivity.class);
                EmailRegistrationActivity.this.startActivity(intent);
                closeActivity();
            }
        });
    }



    public void initFullRegistrationProcess() {

        if(RegisterDataFilter.checkRegDataCorrectness(this, ETemail.getText().toString().trim(),
                ETpassword.getText().toString().trim(),
                ETpasswordConfirmation.getText().toString().trim(),
                ETphone.getText().toString().trim()))
        {
            registerWithEmailAndPassword(ETemail.getText().toString().trim(), ETpassword.getText().toString().trim());
        }
    }



    private void registerWithEmailAndPassword(final String email, final String password)
    {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(EmailRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.child("gmail").setValue(email);
                    databaseReference.child("points").setValue(0);
                    databaseReference.child("phone").setValue("");

                    regStage = "телефон НЕ подтверждён или НЕ указан, регистрация успешна";
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!ETphone.getText().toString().equals(""))
                            {
                                bRegister.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PhoneLinker.linkPhoneToAccount(ETphone.getText().toString().trim(), EmailRegistrationActivity.this);
                                    }
                                });
                                PhoneLinker.linkPhoneToAccount(replaceFirstSymbolsInPhone(ETphone.getText().toString().trim()), EmailRegistrationActivity.this);

                            }
                            else
                                EmailRegistrationActivity.this.closeActivity();
                        }
                    });



                } else
                    Toast.makeText(EmailRegistrationActivity.this, "Пользователь с таким имэйлом уже есть", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String replaceFirstSymbolsInPhone(String phone)
    {
        String rightPhone;
        if(phone.charAt(0) == '8')
            rightPhone = phone.replaceFirst("8", "+7");
        else
            rightPhone = phone;
        return rightPhone;
    }


    private void closeActivity() {
        this.finish();
    }

    protected void onStart()
    {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter("PhoneConfirmed"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(EmailRegistrationActivity.this, regStage, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


}