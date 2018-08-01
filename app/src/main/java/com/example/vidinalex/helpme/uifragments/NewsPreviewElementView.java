package com.example.vidinalex.helpme.uifragments;

import java.util.ArrayList;

public class NewsPreviewElementView {
    final public static boolean POST_LOAD_FROM_CLOUD = true;
    final public static boolean POST_LOAD_FROM_CACHE = false;

    public ArrayList<String> imagesArrayList;
    public String date;
    public String head;
    public String body;
    public boolean loadFrom;

    public NewsPreviewElementView(ArrayList<String> imagesArrayList,
                                  String date, String head, String body, boolean loadFrom)
    {
        this.loadFrom = loadFrom;
        this.body = body;
        this.head = head;
        this.date = date;
        this.imagesArrayList = imagesArrayList;
    }
}
