package com.example.vidinalex.helpme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.fragments.NewsPreviewElementView;
import com.example.vidinalex.helpme.helpers.GlobalVars;
import com.example.vidinalex.helpme.helpers.PermissionManager;
import com.example.vidinalex.helpme.toolbar.LeftSideToolbarInitializator;
import com.mikepenz.materialdrawer.Drawer;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager.checkPermissionsAndRequest(this, PermissionManager.DEFAULT_PERMISION_PACK);

        GlobalVars.setFileSavingPath(getApplicationContext().getFilesDir().toString() + File.separator);



        updateNews();


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

    private void updateNews()
    {
        if(true)
        {
            //TODO грузим новости из кэша
            RecyclerView feedLayout = findViewById(R.id.list);


            NewsPreviewElementView newsPreviewElementView = new NewsPreviewElementView(feedLayout.getContext());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            feedLayout.setLayoutManager(linearLayoutManager);

            //TODO https://developer.android.com/guide/topics/ui/layout/recyclerview это ресайкл вью, если не выйдет юзать его, юзаем линеар, но это уёбищно






        }
    }




}