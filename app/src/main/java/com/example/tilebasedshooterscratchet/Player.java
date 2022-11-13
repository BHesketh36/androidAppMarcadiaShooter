package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Paint;

import androidx.core.content.ContextCompat; // needed for access to custom colours in colors.xml

/**
 * Player is the main character that is controlled by the user.
 * The player class derives from the abstract Character class.
 * Common aspects between the player and enemy are shared there.
 */

class Player extends Character {
    public static final float SPEED_PIXELS_PER_SECOND = 300;
    public static final float MAX_SPEED = (float) (SPEED_PIXELS_PER_SECOND / WorldLoop.MAX_UPDATE);
    public static final int MAX_HP = 100;
    /**
     Constant field of speed of player in pixels in each update.
     Fields must be public for the enemy class to also access.
    */
    private final Controller controller;
    private final Paint paint2;

    private int width;
    private int height;

    /*
     private Paint paint; - Moved to Character Class.
     private float posX; - Moved to GameObject Abstract Class.
     private float posY; - Moved to GameObject Abstract Class.
     private float speedX; - Moved to GameObject Abstract Class.
     private float speedY; - Moved to GameObject Abstract Class.
    */

    Bitmap player;
    private Hp hp;
    private int health;

    // Constructor
    public Player(Context context, Controller controller, float posX, float posY, float radius, Resources res) {
        super(context, ContextCompat.getColor(context, R.color.splinterGreen), posX, posY, radius);
        this.controller = controller;
        this.hp = new Hp(context,this);
        this.health = MAX_HP;
        player = BitmapFactory.decodeResource(res, R.drawable.player);

        width = player.getWidth();
        height = player.getHeight();

        width /= 4; // 32/4 = 8
        height /= 4; // = 8

        this.posX = posX;
        this.posY = posY;

        // Test Paint for Player sprite in case.
        paint = new Paint(); // New instance of the paint class.
        int colour = ContextCompat.getColor(context, R.color.splinterGreen);
        paint.setColor(colour);

        paint2 = new Paint();
        int colour2 = ContextCompat.getColor(context, R.color.invisible);
        paint2.setColor(colour2);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(player, posX, posY, paint);
        canvas.drawCircle(posX, posY, radius, paint2);
        hp.draw(canvas);
        /**
         Radius is currently set in getSize() method in Character class.
        Reds use the same radius variable.
        */

        /*
         Old method to import custom sprite results in a crash.
         canvas.drawBitmap(player, new Rect(0,0,32,32), rectangle, null);
         null means my full image will be used of the player sprite (32 x 32).
        */
    }

    public void update() {
        /** getSetX and getSetY values are gathered from methods in
        Controller.java in public getSetX() and public getSetY() */
        speedX = controller.getSetX()*MAX_SPEED;
        speedY = controller.getSetY()*MAX_SPEED;
        posX += speedX;
        posY += speedY;

        if (speedX != 0 || speedY != 0) {
            // Normalisation of the speed of the Player by considering direction.
            float distance = Misc.getDistanceBetween(0, 0, speedX, speedY);
            rayCastX = speedX / distance;
            rayCastY = speedY / distance;

        }
    }

    public void setPosition(float posX, float posY) { // originally converted from integer in World.java
        this.posX = posX;
        this.posY = posY;
    }

    public float getHealth() {
        return health;
    }

    public void setHp(int health) {
        if (health >= 0) // Fixes bug which caused current health to drop to negative values.
        this.health = health;
    }
}
