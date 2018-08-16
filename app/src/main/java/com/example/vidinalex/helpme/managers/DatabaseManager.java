package com.example.vidinalex.helpme.managers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vidinalex.helpme.datatypes.ListenableString;
import com.example.vidinalex.helpme.datatypes.NewsDateFormat;
import com.example.vidinalex.helpme.uifragments.NewsUnit;
import com.example.vidinalex.helpme.utils.GlobalVars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseManager extends Thread{
    public final static String ACTION_ARRAY_LIST_READY = "asReady";
    public final static String ACTION_NEWS_UNIT_DATA_READY = "newsUnit data ready";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference().child("news");

    private final ListenableString dateListenableString = new ListenableString("");
    private final ListenableString headListenableString = new ListenableString("");
    private final ListenableString bodyListenableString = new ListenableString("");
    private final ListenableString internalBodyListenableString = new ListenableString("");
    private final ListenableString imagesCountListenableString = new ListenableString("");

    private int count = 0;

    private NewsUnit newsUnit;


//    public DatabaseManager()
//    {
//        database.setPersistenceEnabled(true);
//    }



    public static ArrayList<NewsDateFormat> getNewsDatesListFromCloud()
    {
        final ArrayList<NewsDateFormat> as = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext())
                {
                    String s= iterator.next().getKey();
                    Log.d("PostDateDownloaded", s);
                    as.add(new NewsDateFormat(s, NewsUnit.POST_LOAD_FROM_CLOUD));
                    Log.d("PostDateDownloaded", String.valueOf(as.get(as.size()-1).getDate()));
                }
                Intent intent = new Intent(ACTION_ARRAY_LIST_READY);
                GlobalVars.getContext().sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Post Load Failed", "Failllll");
            }
        });
        return as;
    }




    public void assembleNewsUnit(final NewsUnit newsUnit)
    {
        this.newsUnit = newsUnit;
        setListeners(newsUnit.newsDate);


//         Log.d("DatabaseManager",
//                 Thread.currentThread().getName() + ": " + dateListenableString.getString());
//         Log.d("DatabaseManager",
//                 Thread.currentThread().getName() + ": " + headListenableString.getString());
//         Log.d("DatabaseManager",
//                 Thread.currentThread().getName() + ": " + bodyListenableString.getString());
//         Log.d("DatabaseManager",
//                 Thread.currentThread().getName() + ": " + internalBodyListenableString.getString());

    }







    private void setListeners(final String newsDate)
    {
        Log.d("DatabaseManager", "Setting Listeneres...");

        IncrementalMyDataChangeListener incrementalMyDataChangeListener = new IncrementalMyDataChangeListener();
        dateListenableString.setMyDataChangeListener(incrementalMyDataChangeListener);
        headListenableString.setMyDataChangeListener(incrementalMyDataChangeListener);
        bodyListenableString.setMyDataChangeListener(incrementalMyDataChangeListener);
        internalBodyListenableString.setMyDataChangeListener(incrementalMyDataChangeListener);
        imagesCountListenableString.setMyDataChangeListener(incrementalMyDataChangeListener);

        setBodyListener(newsDate);
        setDateListener(newsDate);
        setHeadListener(newsDate);
        setImagesCountListener(newsDate);
        setInternalBodyListener(newsDate);
        Log.d("DatabaseManager", "Listeners set.");
    }

    private void setImagesCountListener(final String newsDate)
    {
        databaseReference.child(newsDate).child("imagesCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagesCountListenableString.setString(String.valueOf(dataSnapshot.getValue()));
                Log.d("DatabaseManager", "imagesCount got: " + imagesCountListenableString.getString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("database error", "no data imagesCount access");
            }
        });
    }

    private void setDateListener(String newsDate)
    {
        dateListenableString.setString(newsDate.substring(4,6) + "." + newsDate.substring(2,4) + ".20" + newsDate.substring(0,2));
    }

    private void setHeadListener(String newsDate)
    {
        databaseReference.child(newsDate).child("head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                headListenableString.setString(dataSnapshot.getValue(String.class));
                Log.d("DatabaseManager", "head got");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setBodyListener(String newsDate)
    {
        databaseReference.child(newsDate).child("body").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bodyListenableString.setString(dataSnapshot.getValue(String.class));
                Log.d("DatabaseManager", "body got");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInternalBodyListener(String newsDate)
    {
        databaseReference.child(newsDate).child("newsDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                internalBodyListenableString.setString(dataSnapshot.getValue(String.class));
                Log.d("DatabaseManager", "internalBody got");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class IncrementalMyDataChangeListener implements ListenableString.MyDataChangeListener
    {
        @Override
        public void onDataChange() {
            count++;
            Log.d("DatabaseManager", ": count++");
            if(count==5)
            {
                initNewsUnit();
                Intent intent = new Intent(ACTION_NEWS_UNIT_DATA_READY + " " + newsUnit.newsDate);
                GlobalVars.getContext().sendBroadcast(intent);
                Log.d("DatabaseManager", " Intent sent");
            }
        }
    }

    private void initNewsUnit()
    {
        newsUnit.internalBody = internalBodyListenableString.getString();
        newsUnit.body = bodyListenableString.getString();
        newsUnit.date = dateListenableString.getString();
        newsUnit.head = headListenableString.getString();

        ArrayList<String> imagesArrayList = new ArrayList<>();

        for (int i = 1; i <= Integer.parseInt(imagesCountListenableString.getString()); i++) {
            imagesArrayList.add(newsUnit.newsDate + "_" + String.valueOf(i));
        }

        newsUnit.imagesArrayList = imagesArrayList;
    }

}









