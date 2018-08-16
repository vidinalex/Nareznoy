package com.example.vidinalex.helpme.uifragments;

import java.util.ArrayList;

public class NewsUnit {
    final public static boolean POST_LOAD_FROM_CLOUD = true;
    final public static boolean POST_LOAD_FROM_CACHE = false;

    public ArrayList<String> imagesArrayList;
    public String newsDate;
    public String date;
    public String head;
    public String body;
    public String internalBody;

    public NewsUnit(ArrayList<String> imagesArrayList,
                    String date, String head, String body, String internalBody)
    {
        this.body = body;
        this.head = head;
        this.date = date;
        this.imagesArrayList = imagesArrayList;
        this.internalBody = internalBody;
    }

    public NewsUnit(String newsDate)
    {
        this.newsDate = newsDate;
    }


}
