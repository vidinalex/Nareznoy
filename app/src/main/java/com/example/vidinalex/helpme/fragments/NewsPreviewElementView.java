package com.example.vidinalex.helpme.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.FrameLayout;

import com.example.vidinalex.helpme.R;

public class NewsPreviewElementView extends FrameLayout {

    public NewsPreviewElementView(Context context) {
        super(context);
        init(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(Context context)
    {
        inflate(context, R.layout.news_preview_element, this);
    }
}
