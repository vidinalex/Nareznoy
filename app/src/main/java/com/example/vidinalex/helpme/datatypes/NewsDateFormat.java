package com.example.vidinalex.helpme.datatypes;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsDateFormat implements Comparable<NewsDateFormat> {

    private String date = "";
    private boolean loadFrom;

    public NewsDateFormat (String date, boolean loadFrom)
    {
        setDate(date);
        setLoadFrom(loadFrom);
    }

    private boolean checkFormat(String date)
    {
        Pattern pattern = Pattern.compile("^[0-9]{2}[01]{1}[0-9]{1}[0-3]{1}[0-9]{1}[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}[0-5]{1}[0-9]{1}$");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if(checkFormat(date))
            this.date = date;
        else
            Log.d("NewsDateFormat", "IllegalDateFormat");
    }

    public boolean getLoadFrom()
    {
        return loadFrom;
    }

    public void setLoadFrom(boolean loadFrom) {
        this.loadFrom = loadFrom;
    }

    @Override
    public int compareTo(@NonNull NewsDateFormat o) {
        int yymmddThis = Integer.parseInt(date.substring(0,5));
        int yymmddThat = Integer.parseInt(o.getDate().substring(0,5));
        if(yymmddThis > yymmddThat)
            return -1;
        if(yymmddThis == yymmddThat)
        {
            yymmddThis = Integer.parseInt(date.substring(6,11));
            yymmddThat = Integer.parseInt(o.getDate().substring(6,11));

            if(yymmddThis > yymmddThat)
                return -1;
            if(yymmddThis == yymmddThat)
                return 0;
            if(yymmddThis < yymmddThat)
                return 1;
        }
        if(yymmddThis < yymmddThat)
            return 1;
        return 0;
    }
}


















