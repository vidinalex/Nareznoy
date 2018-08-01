package com.example.vidinalex.helpme.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.vidinalex.helpme.uifragments.PhoneVerificationActivityDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneLinker extends BroadcastReceiver{

    private static ListenableString verificationCode = new ListenableString("");

    public static void linkPhoneToAccount(final String phone, final AppCompatActivity activity)
    {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        PhoneAuthProvider.getInstance().verifyPhoneNumber
                (phone, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {

                        user.linkWithCredential(phoneAuthCredential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(user.getUid());

                                    databaseReference.child("phone").setValue(phone);
                                    activity.sendBroadcast(new Intent().setAction("PhoneConfirmed"));
                                    activity.finish();
                                }
                                else
                                {
                                    Toast.makeText(activity, "пользователь с таким номером уже зареган", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(activity, "на этот номер невозможно отправить смс: повторите попытку позже", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(final String s, final PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {

                        super.onCodeSent(s, forceResendingToken);

                        Intent intent = new Intent(activity, PhoneVerificationActivityDialog.class);
                        activity.startActivity(intent);

                        verificationCode.setMyDataChangeListener(new ListenableString.MyDataChangeListener() {
                            @Override
                            public void onDataChange() {

                                final PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(s, verificationCode.getString());


                                user.linkWithCredential(phoneAuthCredential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(user.getUid());

                                            databaseReference.child("phone").setValue(phone);
                                            activity.sendBroadcast(new Intent().setAction("PhoneConfirmed"));
                                            activity.finish();
                                        }
                                        else
                                            Toast.makeText(activity, "пользователь с таким номером уже зареган или код неверен", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });



                    }

                });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        verificationCode.setString(intent.getStringExtra("verificationCode"));
    }

}



