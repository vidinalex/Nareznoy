package com.example.vidinalex.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES2 = "mysettings2";
    public static final String APP_PREFERENCES_EMAIL = "";
    public static final String APP_PREFERENCES_PASSWORD = "";
    private TextView refToReg;

    private SharedPreferences mSettings;
    private SharedPreferences mSettings2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mSettings2 = getSharedPreferences(APP_PREFERENCES2, Context.MODE_PRIVATE);



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);

                } else {
                    // User is signed out

                }

            }
        };

        ETemail = (EditText) findViewById(R.id.etEmail);
        ETpassword = (EditText) findViewById(R.id.etPassword);

        refToReg = (TextView) findViewById(R.id.tvRegisterHere);
        refToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        if (mSettings.contains(APP_PREFERENCES_EMAIL)) {
            ETemail.setText(mSettings.getString(APP_PREFERENCES_EMAIL,null));
            ETpassword.setText(mSettings2.getString(APP_PREFERENCES_PASSWORD,null));
            signin(ETemail.getText().toString(),ETpassword.getText().toString());

        }

        findViewById(R.id.bLogin).setOnClickListener(this);
        //findViewById(R.id.bRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bLogin)
        {
            if(!ETemail.getText().toString().equals("") && !ETpassword.getText().toString().equals(""))
            signin(ETemail.getText().toString(),ETpassword.getText().toString());
        }
    }

    public void signin(final String email , final String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_EMAIL, email);
                    editor.commit();
                    SharedPreferences.Editor editor2 = mSettings2.edit();
                    editor2.putString(APP_PREFERENCES_PASSWORD,password);
                    editor2.commit();

                }else
                    Toast.makeText(LoginActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }
}