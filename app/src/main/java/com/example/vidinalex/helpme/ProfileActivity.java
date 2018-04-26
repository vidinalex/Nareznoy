package com.example.vidinalex.helpme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;

public class ProfileActivity extends AppCompatActivity{

    private Drawer.Result drawerResult = null;
    FirebaseDatabase firebaseDatabase = null;
    FirebaseAuth firebaseAuth = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        drawerResult = LeftSideToolbarInitializator.initNewToolbar(this);



        final ImageView qrCode = findViewById(R.id.qrCode);
        Bitmap qrCodeBitmap = QrCodeGenerator.generateQRCode(firebaseAuth.getUid());
        qrCode.setImageBitmap(qrCodeBitmap);



        final TextView gmail = findViewById(R.id.gmail);

        DatabaseReference userDatabaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());


        userDatabaseReference.child("gmail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gmail.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView points = findViewById(R.id.points);


        userDatabaseReference.child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                points.setText(dataSnapshot.getValue().toString() + " pts");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

}
