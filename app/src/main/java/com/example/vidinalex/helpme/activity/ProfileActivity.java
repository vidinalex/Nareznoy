package com.example.vidinalex.helpme.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidinalex.helpme.helpers.GlobalVars;
import com.example.vidinalex.helpme.helpers.InternetChecker;
import com.example.vidinalex.helpme.toolbar.LeftSideToolbarInitializator;
import com.example.vidinalex.helpme.helpers.PermissionManager;
import com.example.vidinalex.helpme.helpers.QrCodeGenerator;
import com.example.vidinalex.helpme.R;
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

        PermissionManager.checkPermissionsAndRequest(this, PermissionManager.DEFAULT_PERMISION_PACK);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        drawerResult = LeftSideToolbarInitializator.initNewToolbar(this);

        Toast.makeText(this, GlobalVars.getFileSavingPath(), Toast.LENGTH_LONG).show();

        setQrCodeImage();

        setPtsAndEmailFields();

    }


    private void setQrCodeImage()
    {
        final ImageView qrCode = findViewById(R.id.qrCode);
        final TextView points = findViewById(R.id.points);

        if(InternetChecker.checkInternet(this))
        {
            final String uid = firebaseAuth.getUid();

            Bitmap qrCodeBitmap = QrCodeGenerator.generateQRCodeBitmap(uid);
            QrCodeGenerator.writeUidToCache(uid);
            qrCode.setImageBitmap(qrCodeBitmap);
        }
        else
        {
            qrCode.setImageBitmap(QrCodeGenerator.generateQRCodeBitmap(QrCodeGenerator.readUidFromCache()));

            switch (QrCodeGenerator.readUidFromCache())
            {
                case "No sd card":
                    points.setText("Нет карты памяти"); break;
                case "No QR-Codes cached":
                    points.setText("Ни одного кода не закэшровано"); break;
                default: qrCode.setImageBitmap(QrCodeGenerator.generateQRCodeBitmap(QrCodeGenerator.readUidFromCache()));
                    break;
            }
        }
    }






    private void setPtsAndEmailFields()
    {
        final TextView gmail = findViewById(R.id.gmail);
        final TextView points = findViewById(R.id.points);

        if(InternetChecker.checkInternet(this))
        {
            DatabaseReference userDatabaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid());

            userDatabaseReference.child("gmail").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    gmail.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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
        else
        {
            points.setText("QR-код последнего пользователя");
            gmail.setText("НЕТ ИНТЕРНЕТА");
        }
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
