package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

class Medic extends Character {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.2;
    // Multiplying by a double causes the enemy to move at a fraction of the speed of player.
    private static final double MAX_SPEED = (SPEED_PIXELS_PER_SECOND / WorldLoop.MAX_UPDATE);
    // Constant fields are set to public within Player.java so that they can be accessed.
    private final Player player;

    Bitmap blueSprite;

    public Medic(Context context, Player player, float posX, float posY, float radius, Resources res) {
        super(context, ContextCompat.getColor(context, R.color.friend), posX, posY, radius);
        this.player = player;
        blueSprite = BitmapFactory.decodeResource(res, R.drawable.friend);
        /*
         replaced colour argument with Player player.
         Declared this player to match the Player Class.
        */
    }

    public Medic(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.scuderiaRed),
                /** Random spawn location */
                //Random value generated between 0 and 1000.
                (float) (Math.random() * 1000), (float) (Math.random() * 1000),
                30);
        //Fixed - random function works with doubles : posX, posY are casted with floats to round later.
        this.player = player; // Stores the player
    }

    @Override
    public void update() {
        /**
         * The update method is overridden so that the controller does not control the enemies too.
         * Instead, they will update their speed so that they chase the player.
         * */

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
        canvas.drawBitmap(blueSprite, posX, posY, paint);
    }
}