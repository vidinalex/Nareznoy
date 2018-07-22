package com.example.vidinalex.helpme;

public class ListenableString {

    public interface MyDataChangeListener
    {
        public void onDataChange();
    }

    private String s;
    private MyDataChangeListener listener;

    ListenableString(String s)
    {
        this.listener = null;
        this.s = s;
    }


    public String getString()
    {
        return s;
    }

    public void setString(String s)
    {
        this.s = s;
        listener.onDataChange();
    }


    public void setMyDataChangeListener(MyDataChangeListener listener)
    {
        this.listener = listener;
    }

}
