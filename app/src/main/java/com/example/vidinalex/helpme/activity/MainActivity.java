package com.example.vidinalex.helpme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.toolbar.LeftSideToolbarInitializator;
import com.example.vidinalex.helpme.uifragments.NewsPreviewElementView;
import com.example.vidinalex.helpme.uifragments.NewsRecyclerViewAdapter;
import com.example.vidinalex.helpme.utils.GlobalVars;
import com.example.vidinalex.helpme.utils.PermissionManager;
import com.mikepenz.materialdrawer.Drawer;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;

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
        ArrayList<NewsPreviewElementView> arrayList = new ArrayList<>();
        arrayList = getListWithCachedNews(arrayList);

        //TODO грузим из облака и кэшируем, а ещё ебёмся с подгрузкой из кэша

        initRecyclerView(arrayList);

    }

    private ArrayList<NewsPreviewElementView> getListWithCachedNews(ArrayList<NewsPreviewElementView> arrayList)
    {
        NewsPreviewElementView newsPreviewElementView;


        //TODO настроить подгрузку новостей из кэша(общий файл с записями о новостях - сборка новости по кускам)
        ArrayList<String> as = new ArrayList<>();
        as.add("clip-art-12.png");

        for (int i = 0; i < 4000; i++) {

            as = new ArrayList<>();
            as.add("clip-art-12.png");

            newsPreviewElementView = new NewsPreviewElementView
                    (as, String.valueOf(i), "Head",
                            "body", NewsPreviewElementView.POST_LOAD_FROM_CLOUD);
            arrayList.add(newsPreviewElementView);
        }
        return arrayList;
    }



    private void initRecyclerView(ArrayList<NewsPreviewElementView> arrayList)
    {
        RecyclerView feedLayout = findViewById(R.id.list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        feedLayout.setLayoutManager(layoutManager);

        feedLayout.setAdapter(new NewsRecyclerViewAdapter(arrayList));
    }




}