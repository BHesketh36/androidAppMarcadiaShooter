package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

class Background {
    private int height; // set private as fields with same names belong in Player.java.
    private int width;
    private float posBgX; // double data type not compatible with drawBitmap function so its a float.
    private float posBgY;
    private Paint paint;
    Bitmap background;
    Bitmap bg;
    Bitmap pain;
    Bitmap painite;
    Bitmap alex;
    Bitmap alexandrite;
    Bitmap opal;
    Bitmap opal_block;
    Bitmap reds1;

    private float posPainX;
    private float posPainY;
    private float posX;
    private float posY;

    //constructor
    public Background(Context context, float posBgX , float posBgY, Resources res) {

        bg = BitmapFactory.decodeResource(res, R.drawable.battleground);
        background = Bitmap.createScaledBitmap(bg, 2200, 1080, true);
        pain = BitmapFactory.decodeResource(res, R.drawable.painite_ore_block);
        painite = Bitmap.createScaledBitmap(pain, 64, 64, true);
        alex = BitmapFactory.decodeResource(res, R.drawable.alexandrite_ore_block);
        alexandrite = Bitmap.createScaledBitmap(alex, 64, 64, true);
        opal = BitmapFactory.decodeResource(res, R.drawable.opal_block);
        opal_block = Bitmap.createScaledBitmap(opal, 64, 64, true);

        this.posBgX = posBgX;
        this.posBgY = posBgY;

        paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.splinterGreen);
        paint.setColor(colour);
    }

    public void draw(Canvas canvas) {
        // Draw the final rescaled images below.
        canvas.drawBitmap(background, posBgX, posBgY, paint);
        canvas.drawBitmap(painite, posPainX, posPainY, paint);
        canvas.drawBitmap(alexandrite, 1500, 800, paint);
        canvas.drawBitmap(opal_block, 2000, 200, paint);
    }

    public void setPos(Canvas canvas) {
        // canvas must be mentioned for the argument of same name to be expected in super.draw(canvas) in World.java
        posBgX = 10;
        posBgY = -10;
        posPainX = 1000;
        posPainY = 100;
    }

    public void setSize(Canvas canvas) {
        width = background.getWidth();
        height = background.getHeight();
        // original size of drawing on canvas is 222 * 108, galaxy S8 default res is 2220 * 1080.
    }
}
