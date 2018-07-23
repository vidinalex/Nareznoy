package com.example.vidinalex.helpme;

import android.graphics.Bitmap;
import android.os.Environment;

import net.glxn.qrgen.android.QRCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class QrCodeGenerator {

    public static Bitmap generateQRCodeBitmap(String userId)
    {
        return QRCode.from(userId).withSize(2000,2000).bitmap();
    }

    public static void writeUidToCache(String userId)
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try{
            File file = new File
                    (Environment.getExternalStorageDirectory() + File.separator + "lastUser.txt");
            //file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(userId.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readUidFromCache(){

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return "No sd card";
            }

            File file = new File
                    (Environment.getExternalStorageDirectory() + File.separator + "lastUser.txt");

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                return br.readLine();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "No QR-Codes cached";
        }

}
