package com.example.vidinalex.helpme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class NoInternetActivity extends AppCompatActivity {


    //TODO понять точки входа и выхода этого активити(наверна интернет конэкшн ресивер или типа того) мб намутить глобальный ресивер и при каждом интенте на запуск активити чекать инет
    //https://ru.stackoverflow.com/questions/523668/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%BD%D0%BE-%D0%BF%D1%80%D0%BE%D0%B2%D0%B5%D1%80%D1%8F%D1%82%D1%8C-%D0%B8%D0%BC%D0%B5%D0%B5%D1%82%D1%81%D1%8F-%D0%BB%D0%B8-%D0%BF%D0%BE%D0%B4%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BA-%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D0%BD%D0%B5%D1%82%D1%83?rq=1
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Bitmap QrBitmap = null;
        ImageView imageView = findViewById(R.id.qrCode);
        TextView textView = findViewById(R.id.message);

        switch (QrCodeGenerator.readUidFromCache())
        {
            case "No sd card":
                textView.setText("Нет карты памяти"); break;
            case "No QR-Codes cached":
                textView.setText("Ни одного кода не закэшровано"); break;
            default: QrBitmap = QrCodeGenerator.generateQRCodeBitmap(QrCodeGenerator.readUidFromCache());
                imageView.setImageBitmap(QrBitmap);
                break;
        }


    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

    }
}
