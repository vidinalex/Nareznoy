package com.example.vidinalex.helpme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.datatypes.NewsDateFormat;
import com.example.vidinalex.helpme.managers.DatabaseManager;
import com.example.vidinalex.helpme.managers.FileManager;
import com.example.vidinalex.helpme.managers.PermissionManager;
import com.example.vidinalex.helpme.toolbar.LeftSideToolbarInitializator;
import com.example.vidinalex.helpme.uifragments.NewsUnitAdapter;
import com.example.vidinalex.helpme.utils.GlobalVars;
import com.mikepenz.materialdrawer.Drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;
    private String ACTION_ARRAY_LIST_READY = "asReady";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager.checkPermissionsAndRequest(this, PermissionManager.DEFAULT_PERMISSION_PACK);

        GlobalVars.setFileSavingPath(getApplicationContext().getFilesDir().toString() + File.separator);

        GlobalVars.setContext(getApplicationContext());

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
        DatabaseManager databaseManager = new DatabaseManager();
        final ArrayList<NewsDateFormat> as = FileManager.getCachedNewsDatesList();
        final ArrayList<NewsDateFormat> as2 = databaseManager.getNewsDatesListFromCloud();
        databaseManager = null;

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(as.size() != 0)
                {
                    Log.d("ArrayList", "ArrayList not empty");
                    int date = Integer.parseInt(as.get(as.size()-1).getDate());
                    int date2;

                    for (int i = as2.size()-1; i >= 0; i++) {
                        date2 = Integer.parseInt(as2.get(i).getDate());
                        if(date2>date)
                        {
                            as.add(as.size(),as2.get(i));
                        }
                        else
                            break;
                    }
                }
                else
                {
                    Log.d("ArrayList", "ArrayList empty");
                    as.addAll(as2);
                }

                Log.d("ArrayList", "AS size is " + String.valueOf(as.size()));

                for (int i = 0; i < as.size(); i++) {
                    Log.d("ArrayList", as.get(i).getDate() + " " + as.get(i).getLoadFrom());
                }

                Collections.sort(as);


                unregisterReceiver(this);
                initRecyclerView(as);
            }
        }, new IntentFilter(ACTION_ARRAY_LIST_READY));
    }


    private void initRecyclerView(final ArrayList<NewsDateFormat> arrayList)
    {
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d("initRecyclerView AS", arrayList.get(i).getDate() + " " + arrayList.get(i).getLoadFrom());
        }

        final RecyclerView feedLayout = findViewById(R.id.list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        feedLayout.setLayoutManager(layoutManager);

        feedLayout.setAdapter(new NewsUnitAdapter(arrayList));
    }




}