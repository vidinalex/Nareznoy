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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    private EditText ETemail;
    private EditText ETpassword;
    private EditText ETpasswordConfirmation;
    private Button refToLog;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };

        ETemail = (EditText) findViewById(R.id.etEmail);
        ETpassword = (EditText) findViewById(R.id.etPassword);
        ETpasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        refToLog = (Button) findViewById(R.id.bRefLogin);

        //findViewById(R.id.bLogin).setOnClickListener(this);
        findViewById(R.id.bRegister).setOnClickListener(this);

        refToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                closeActivity();
            }
        });
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bRegister) {
            String email = ETemail.getText().toString();
            if(email.substring(0, email.indexOf("@")).length() >=6)
            {
                if (ETpassword.getText().toString().length() >= 6)
                {
                   if (ETpassword.getText().toString().equals(ETpasswordConfirmation.getText().toString()))
                       registration(ETemail.getText().toString(), ETpassword.getText().toString());
                   else
                       Toast.makeText(RegisterActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Пароль не меенее 6 символов", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(RegisterActivity.this, "Имэйл не менее 6 символов", Toast.LENGTH_SHORT).show();
        }

    }


    public void registration (final String email , String password) {


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getUid());
                        databaseReference.child("gmail").setValue(email);
                        databaseReference.child("points").setValue(0);

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        closeActivity();

                    } else
                        Toast.makeText(RegisterActivity.this, "Пользователь с таким имэйлом уже есть", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void closeActivity() {
        this.finish();
    }
}