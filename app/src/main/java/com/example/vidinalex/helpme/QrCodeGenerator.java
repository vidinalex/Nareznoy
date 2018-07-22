package com.example.vidinalex.helpme;

import android.graphics.Bitmap;

import net.glxn.qrgen.android.QRCode;

public class QrCodeGenerator {

    public static Bitmap generateQRCode(String userId)
    {
        return QRCode.from(userId).withSize(2000,2000).bitmap();
    }

    public static void generateQRAndWriteToCache(String userId, Bitmap QRCodeBitmap)
    {
        //TODO ну ты понял, кэш, да
    }
}
