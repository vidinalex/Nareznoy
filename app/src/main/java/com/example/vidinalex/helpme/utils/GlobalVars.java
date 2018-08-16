package com.example.vidinalex.helpme.utils;

import android.content.Context;

import java.io.File;

public class GlobalVars {

    private static String fileSavingPath = "";
    private static Context context = null;

    public static void setFileSavingPath(String fileSavingPath) {
        GlobalVars.fileSavingPath = fileSavingPath;
    }

    public static String getFileSavingPath() {
        return fileSavingPath;
    }

    public static void setContext(Context context) {
        GlobalVars.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static File getNewsListFile()
    {
        return new File(GlobalVars.getFileSavingPath() + File.separator + "newsList.txt");
    }
}
