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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailRegistrationActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    private EditText ETemail;
    private EditText ETpassword;
    private EditText ETpasswordConfirmation;
    private EditText ETphone;
    private EditText ETConfirmationCode;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);

        mAuth = FirebaseAuth.getInstance();


        ETemail = (EditText) findViewById(R.id.etEmail);
        ETpassword = (EditText) findViewById(R.id.etPassword);
        ETpasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        ETphone = findViewById(R.id.etPhoneNumber);
        ETConfirmationCode = findViewById(R.id.etConfirmationCode);
        bRegister = findViewById(R.id.bRegister);

        Button refToLog = (Button) findViewById(R.id.bRefLogin);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preRegistration();
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




    public void preRegistration() {

        if(checkRegDataCorrectness())
        {
            registration(ETemail.getText().toString().trim(), ETpassword.getText().toString().trim(), ETphone.getText().toString().trim());
        }

    }

    private boolean checkRegDataCorrectness()
    {
        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();
        String passwordConf = ETpasswordConfirmation.getText().toString().trim();
        String phone = ETphone.getText().toString().trim();

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+\\-/=?^`{|}~]{3,}@[a-zA-Z0-9_]+\\.[a-z0-9_]+$");
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        {
            Toast.makeText(EmailRegistrationActivity.this, "Имэйл не менее 6 симв до @ и по формату", Toast.LENGTH_SHORT).show();
            return false;
        }

        pattern = Pattern.compile("^[a-zA-Z0-9]{6,}$");
        matcher = pattern.matcher(password);
        if(!matcher.matches())
        {
            Toast.makeText(EmailRegistrationActivity.this, "Пароль >= 6 симв; a-zA-z0-9", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(passwordConf))
        {
            Toast.makeText(EmailRegistrationActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!phone.equals(""))
        {
            pattern = Pattern.compile("^((\\+7)|(8))[0-9]{10}$");
            matcher = pattern.matcher(phone);
            if(!matcher.matches())
            {
                Toast.makeText(EmailRegistrationActivity.this, "Телефон не соответствует формату", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    public void registration (final String email , final String password, final String phone)
    {

    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                if(!phone.equals(""))
                {
                    bRegister.setText("Подтвердить номер");
                    String rightPhone;
                    if(phone.charAt(0) == '8')
                        rightPhone = phone.replaceFirst("8", "+7");
                    else
                        rightPhone = phone;


                    verifyPhoneNumberAndLinkAccs(rightPhone, email, password);
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email,password);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.child("gmail").setValue(email);
                    databaseReference.child("points").setValue(0);
                    databaseReference.child("phone").setValue(phone);


                    Toast.makeText(EmailRegistrationActivity.this, "телефон НЕ указан, регистрация успешна", Toast.LENGTH_SHORT).show();
                    closeActivity();
                }


            } else
                Toast.makeText(EmailRegistrationActivity.this, "Пользователь с таким имэйлом уже есть", Toast.LENGTH_SHORT).show();
        }
    });

    }

    private void closeActivity() {
        this.finish();
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







    private void verifyPhoneNumberAndLinkAccs(final String phone, final String email, final String password)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber
                (phone, 60, TimeUnit.SECONDS, EmailRegistrationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {

                        mAuth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnCompleteListener(EmailRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());
                                    databaseReference.child("gmail").setValue(email);
                                    databaseReference.child("points").setValue(0);
                                    databaseReference.child("phone").setValue(phone);


                                    Toast.makeText(EmailRegistrationActivity.this, "телефон подтверждён, регистрация успешна", Toast.LENGTH_SHORT).show();
                                    closeActivity();
                                }
                                else
                                {
                                    mAuth.getCurrentUser().delete();
                                    Toast.makeText(EmailRegistrationActivity.this, "пользователь с таким номером уже зареган", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(EmailRegistrationActivity.this, "верификация провалена", Toast.LENGTH_SHORT).show();
                        mAuth.getCurrentUser().delete();
                    }

                    @Override
                    public void onCodeSent(final String s, final PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        ETConfirmationCode.setVisibility(View.VISIBLE);
                        bRegister.setText("ПРОДОЛЖИТЬ"); //ytyflf
                        bRegister.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!ETConfirmationCode.getText().toString().equals(""))
                                {
                                    final PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(s,ETConfirmationCode.getText().toString());

                                    mAuth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnCompleteListener(EmailRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())
                                            {
                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());
                                                databaseReference.child("gmail").setValue(email);
                                                databaseReference.child("points").setValue(0);
                                                databaseReference.child("phone").setValue(phone);


                                                Toast.makeText(EmailRegistrationActivity.this, "телефон подтверждён, регистрация успешна", Toast.LENGTH_SHORT).show();
                                                closeActivity();
                                            }
                                            else
                                                Toast.makeText(EmailRegistrationActivity.this, "пользователь с таким номером уже зареган или код неверен", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                                else
                                    Toast.makeText(EmailRegistrationActivity.this, "Введите код", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

}