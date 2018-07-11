package com.example.vidinalex.helpme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private EditText etPhoneNumber;
    private EditText etConfirmationCode;
    private Button bRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();

        Button refToLog = (Button) findViewById(R.id.bRefLogin);

        refToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO ref to login on phone
            }
        });

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etConfirmationCode = findViewById(R.id.etConfirmationCode);
        bRegistration = findViewById(R.id.bRegister);

        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPhoneNumber.getText().toString().equals(""))
                    registration(etPhoneNumber.getText().toString());
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
    public void onClick(View view) {
        if(view.getId() == R.id.refToEmail)
        {
            Intent intent = new Intent(this, EmailRegistrationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void registration(final String phone)
    {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(final String s, final PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                etConfirmationCode.setVisibility(View.VISIBLE);
                bRegistration.setText("ПРОДОЛЖИТЬ");
                bRegistration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!etConfirmationCode.getText().toString().equals(""))
                        {
                            final PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(s,etConfirmationCode.getText().toString());
                            mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {


                                        Toast.makeText(PhoneLoginActivity.this, "вход успешна", Toast.LENGTH_SHORT).show();

                                        PhoneLoginActivity.this.finish();
                                    }
                                    else
                                        Toast.makeText(PhoneLoginActivity.this, "вход провалена", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(PhoneLoginActivity.this, "Введите код", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber
                (phone, 0, TimeUnit.SECONDS, PhoneLoginActivity.this, Callbacks);

    }
}
