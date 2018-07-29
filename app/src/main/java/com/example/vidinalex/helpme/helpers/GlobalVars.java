package com.example.vidinalex.helpme.helpers;

public class GlobalVars {

    private static String fileSavingPath = "";

    public static void setFileSavingPath(String fileSavingPath) {
        GlobalVars.fileSavingPath = fileSavingPath;
    }

    public static String getFileSavingPath() {
        return fileSavingPath;
    }
}
