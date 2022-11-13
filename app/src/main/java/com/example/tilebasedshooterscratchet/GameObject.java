package com.example.tilebasedshooterscratchet;

/**
 Abstract class means it can no longer be initialised as an object.
 Another class must inherit this gameObject to initialise it.
 GameObject.java is the main abstract class for all objects within the world.
 */

import android.graphics.Canvas;

public abstract class GameObject {
    /** protected means that the variables can be accessed by Player.java class
     and other derived classes only. */
    protected float posX;
    protected float posY;
    protected float speedX;
    protected float speedY;
    protected float radius;
    protected float rayCastX;
    protected float rayCastY;

    // constructor
    public GameObject(float posX, float posY, float radius) {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
    }

    // derived classes must write methods of the same name
    public abstract void draw(Canvas canvas);
    public abstract void update();

    protected float getPosX() {
        return posX;
    }

    protected float getPosY() {
        return posY;
    }

    protected static float getDistanceBetween(GameObject entity1, GameObject entity2) {
        return (float) Math.sqrt(
                Math.pow(entity2.getPosX() - entity1.getPosX(), 2) +
                        Math.pow(entity2.getPosY() - entity1.getPosY(), 2)
        );
    }

    protected float getRayCastX() {
        return rayCastX;
    }

    protected float getRayCastY() {
        return rayCastY;
    }
}
