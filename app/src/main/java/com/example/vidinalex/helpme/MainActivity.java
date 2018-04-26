package com.example.vidinalex.helpme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.materialdrawer.Drawer;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerResult = LeftSideToolbarInitializator.initNewToolbar(this);

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