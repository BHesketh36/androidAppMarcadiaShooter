package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class EndGame {
    private Context context;

    public EndGame(Context context) {
        this.context = context;

    }

    public void draw(Canvas canvas) {
        String text = "Mission Failed, we will get them next time...";

        float posX = 400;
        float posY = 400;

        Paint paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.scuderiaRed);
        paint.setColor(colour);
        float textSize = 50;
        paint.setTextSize(textSize);

        canvas.drawText(text, posX, posY, paint);
    }
}
