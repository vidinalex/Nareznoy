package com.example.vidinalex.helpme.managers;

import com.example.vidinalex.helpme.datatypes.NewsDateFormat;
import com.example.vidinalex.helpme.uifragments.NewsUnit;
import com.example.vidinalex.helpme.utils.GlobalVars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {



    public static NewsUnit assembleNewsUnit(String newsDate)
    {
        File file = new File(GlobalVars.getFileSavingPath() + File.separator + newsDate);
        ArrayList<String> as = new ArrayList<String>();
        as.add(newsDate);
        return new NewsUnit(as, newsDate, "ahsdn", "sjkhdnfhs", "sidfi");
    }

    public static ArrayList<NewsDateFormat> getCachedNewsDatesList()
    {
        ArrayList<NewsDateFormat> as = new ArrayList<>();

        File file = GlobalVars.getNewsListFile();

        try {
            file.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;

        while ((s = br.readLine())!=null)
        {
            as.add(new NewsDateFormat(s, NewsUnit.POST_LOAD_FROM_CACHE));
        }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        return as;
    }


    public static void saveNewsUnitToCache(NewsUnit newsUnit)
    {

    }
}
