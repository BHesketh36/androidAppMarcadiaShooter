package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import androidx.core.content.ContextCompat;

class Boss extends Character {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.3;
    // Multiplying by a double causes the enemy to move at a fraction of the speed of player.
    private static final double MAX_SPEED = (SPEED_PIXELS_PER_SECOND / WorldLoop.MAX_UPDATE);
    // Constant fields are set to public within Player.java so that they can be accessed.
    private final Player player;

    Bitmap redSprite;

    public Boss(Context context, Player player, float posX, float posY, float radius, Resources res) {
        super(context, ContextCompat.getColor(context, R.color.scuderiaRed), posX, posY, radius);
        this.player = player;
        redSprite = BitmapFactory.decodeResource(res, R.drawable.enemy);
    }

    public Boss(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.scuderiaRed),
                //Random value generated between 0 and 1000.
                /** Random spawn location */
                (float) (Math.random() * 1000), (float) (Math.random() * 1000),
                 30);
        //Fixed - random function works with doubles : posX, posY are casted with floats to round later.
        this.player = player; // Stores the player
    }

    @Override
    public void update() {

        //The vector
        float distanceFromPlayerX = player.getPosX() - posX;
        float distanceFromPlayerY = player.getPosY() - posY;

        //The absolute distance between the player and the red needs to be calculated.
        float distanceFromPlayer = GameObject.getDistanceBetween(this, player);

        //The direction (ray cast) so the red faces the player.
        float rayCastX = distanceFromPlayerX / distanceFromPlayer;
        float rayCastY = distanceFromPlayerY / distanceFromPlayer;

        //Sets the speed to chase the player (dependent on distance).
        if (distanceFromPlayer > 0) {
            speedX = (float) (rayCastX*MAX_SPEED);
            speedY = (float) (rayCastY*MAX_SPEED);
        } else {
            speedX = 0;
            speedY = 0;
        }

        //Updates the position of the red within the game world.
        posX += speedX;
        posY += speedY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(redSprite, posX, posY, paint);
    }
}
