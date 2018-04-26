package com.example.vidinalex.helpme;

import android.graphics.Bitmap;

import net.glxn.qrgen.android.QRCode;

public class QrCodeGenerator {

    public static Bitmap generateQRCode(String userId)
    {
        Bitmap myBitmap = QRCode.from(userId).bitmap();
        return myBitmap;
    }
}
