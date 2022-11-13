package com.example.tilebasedshooterscratchet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Resources;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * The Reds are an extension of the Character Abstract Class,
 * which is an extension of the GameObject Abstract Class.
 */

class Reds extends Character {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6;
    // Multiplying by a double causes the enemy to move at a fraction of the speed of player.
    private static final double MAX_SPEED = (SPEED_PIXELS_PER_SECOND / WorldLoop.MAX_UPDATE);
    private static final double EMITS_PER_MINUTE = 20;
    private static final double EMITS_PER_SECOND = EMITS_PER_MINUTE / 60.0; // Converts minutes to seconds
    private static final double UPDATES_PER_EMITS = WorldLoop.MAX_UPDATE / EMITS_PER_SECOND;
    private static double updatesUntilNextEmits  = UPDATES_PER_EMITS;
    // Constant fields are set to public within Player.java so that they can be accessed.
    private final Player player;

    Bitmap redSprite;

    public Reds(Context context, Player player, float posX, float posY, float radius, Resources res) {
        super(context, ContextCompat.getColor(context, R.color.scuderiaRed), posX, posY, radius);
        this.player = player;
        redSprite = BitmapFactory.decodeResource(res, R.drawable.enemy);
        /*
         replaced colour argument with Player player.
         Declared this player to match the Player Class.
        */
    }

    public Reds(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.scuderiaRed),
                //Random value generated between 0 and 1000.
                (float) new Random().nextInt(101) + 2000,
                (float) new Random().nextInt(101) + 2000,
                 30);
        //Fixed - random function works with doubles : posX, posY are casted with floats to round later.
        this.player = player; // Stores the player
    }

    /**
     Checks to see if the maximum number of red team members are currently in the world.
     REDS_PER_MIN is the maximum number at the top of this class.
     */
    public static boolean readyToEmit() {
        if (updatesUntilNextEmits <= 0) {
            updatesUntilNextEmits += UPDATES_PER_EMITS;
            return true; // Allows update method in World Class to spawn new reds.
        }
        else {
            updatesUntilNextEmits --;
            return false;
        }
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
        canvas.drawCircle(posX, posY, 30, paint);
        // Crash (Bitmap returns null) - canvas.drawBitmap(redSprite, posX, posY, paint);
    }
}
