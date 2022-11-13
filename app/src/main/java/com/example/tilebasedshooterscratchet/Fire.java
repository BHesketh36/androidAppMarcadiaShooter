package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/** Free functionality from Character abstract class including collisions */
public class Fire extends Character {

    public static final float SPEED_PIXELS_PER_SECOND = 500;
    public static final float MAX_SPEED = (float) (SPEED_PIXELS_PER_SECOND / WorldLoop.MAX_UPDATE);

    public Fire(Context context, Player shooter) {
        super(context, ContextCompat.getColor(context, R.color.projectile), shooter.getPosX(), shooter.getPosY(), 40);
        speedX = shooter.getRayCastX() * MAX_SPEED;
        speedY = shooter.getRayCastY() * MAX_SPEED;
    }

    @Override
    public void update() {
        posX += speedX;
        posY += speedY;
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(posX, posY, 40, paint);
    }
}
