package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/** Hp is the health points that the player has. */
public class Hp {

    private Player player;
    private int width, height, spacing;
    private Paint boxPaint;
    private Paint hpPaint;


    public Hp(Context context, Player player) {
        this.player = player;
        this.width = 100;
        this.height = 40;
        this.spacing = 3;

        this.boxPaint = new Paint();
        int boxColour = ContextCompat.getColor(context, R.color.scuderiaRed);
        boxPaint.setColor(boxColour);

        this.hpPaint = new Paint();
        int hpColour = ContextCompat.getColor(context, R.color.splinterGreen);
        hpPaint.setColor(hpColour);
        // Added Context context to constructor head so colours can be accessed.

    }

    public void draw(Canvas canvas) {
        float locationX = player.getPosX();
        float locationY = player.getPosY();
        float distanceFromPlayer = 30;
        float hpPercentage = player.getHealth()/Player.MAX_HP;

        // The Hp box.
        float boxLeft, boxTop, boxRight, boxBottom;
        boxLeft = locationX - width / 2;
        boxRight = locationX + width / 2;
        boxBottom = locationY - distanceFromPlayer;
        boxTop = boxBottom - height;
        canvas.drawRect(boxLeft, boxTop, boxRight, boxBottom, boxPaint);

        // The Hp health indicator.
        float hpLeft, hpTop, hpRight, hpBottom, hpWidth, hpHeight;
        hpWidth = width - 2 * spacing;
        hpHeight = height - 2 * spacing;
        hpLeft = boxLeft + spacing;
        hpRight = hpLeft + hpWidth * hpPercentage;
        hpBottom = boxBottom - spacing;
        hpTop = hpBottom - hpHeight;


        canvas.drawRect(hpLeft, hpTop, hpRight, hpBottom, hpPaint);

    }
}
