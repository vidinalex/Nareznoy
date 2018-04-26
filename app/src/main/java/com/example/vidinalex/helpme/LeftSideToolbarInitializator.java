package com.example.vidinalex.helpme;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;

public class LeftSideToolbarInitializator {

    private static Drawer.Result drawerResult = null;

    public static Drawer.Result initNewToolbar(AppCompatActivity activity){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drawerResult = new LeftSideToolbarDrawer(activity, toolbar, drawerResult).build();
        return drawerResult;
    }
}
