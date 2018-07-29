package com.example.vidinalex.helpme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.vidinalex.helpme.toolbar.LeftSideToolbarInitializator;
import com.example.vidinalex.helpme.helpers.PermissionManager;
import com.example.vidinalex.helpme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;


public class SettingsActivity extends AppCompatActivity {

    private Button bToLogOut;
    private FirebaseAuth mAuth;
    private Button bConnectPhone;

    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PermissionManager.checkPermissionsAndRequest(this, PermissionManager.DEFAULT_PERMISION_PACK);

        mAuth = FirebaseAuth.getInstance();

        drawerResult = LeftSideToolbarInitializator.initNewToolbar(this);

        bToLogOut = (Button) findViewById(R.id.bSignout);
        bConnectPhone = findViewById(R.id.bConnectPhone);

        bConnectPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null)
                {
                    Intent intent = new Intent(SettingsActivity.this, PhoneEnterActivity.class);
                    startActivity(intent);
                    SettingsActivity.this.finish();
                }
            }
        });

        bToLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
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
