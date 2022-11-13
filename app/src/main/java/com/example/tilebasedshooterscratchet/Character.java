package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * The Character Abstract Class
 * is an extension of the GameObject Abstract Class.
 */

public abstract class Character extends GameObject {
    protected Paint paint;
    protected float radius;

    public Character(Context context, int colour, float posX, float posY, float radius) {
        super(posX, posY, radius);
        paint = new Paint(); // new instance of the paint class.
        paint.setColor(colour);
    }

    public static boolean isHitting(Character entity1, Character entity2) {
        // Method arguments changed to make method more generic.
        double distance = getDistanceBetween(entity1, entity2);
        double distanceToHitting = entity1.getSize() + entity2.getSize();
        if (distance < distanceToHitting) return true;
        else return false;
    }

    private double getSize() {
        return radius = 30;
    }

    public void draw(Canvas canvas) {
        // Specific sprites are only kept in relevant classes,
        // canvas.drawCircle(posX, posY, radius, paint); - Required in specific Classes of Override method.
    }
}
