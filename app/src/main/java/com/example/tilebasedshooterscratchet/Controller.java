package com.example.tilebasedshooterscratchet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Controller {

    private Paint outerAnalogueColour; // received null which caused runtime exception since declared as Paint again underneath.
    private Paint mainAnalogueColour;
    private int outerAnaloguePosX;
    private int outerAnaloguePosY;
    private int moveAnaloguePosX;
    private int moveAnaloguePosY;
    private int outerRadius;
    private int moveRadius;
    private float controllerCenterToTouchDistance; // has to be double for calculation of analogue location (inner circle).
    private boolean isTouch;
    private float setPosX;
    private float setPosY;
    //private float touchPosX;  now declared below in setPos void method
    //private float touchPosY;  now declared below in setPos void method

    public Controller(int centerX, int centerY, int outerRadius, int moveRadius) {
        // coordinates
        outerAnaloguePosX = centerX;
        outerAnaloguePosY = centerY;
        moveAnaloguePosX = centerX;
        moveAnaloguePosY = centerY;

        // circle radius size
        this.outerRadius = outerRadius; // "this" keyword to distinguish them from the input arguments
        this.moveRadius = moveRadius;

        // colour for analogue shadow background
        outerAnalogueColour = new Paint();
        outerAnalogueColour.setColor(Color.argb(255, 100, 60, 0));
        outerAnalogueColour.setStyle(Paint.Style.FILL);

        // colour for actual analogue stick to move player sprite around
        mainAnalogueColour = new Paint();
        mainAnalogueColour.setColor(Color.argb(255, 220, 220, 220));
        mainAnalogueColour.setStyle(Paint.Style.FILL);
    }

    // rendering analogue stick and background onto the screen
    public void draw(Canvas canvas) {
        // Analogue shadow background
        canvas.drawCircle(outerAnaloguePosX, outerAnaloguePosY, outerRadius, outerAnalogueColour);
        // Actual main analogue stick to move player sprite around
        canvas.drawCircle(moveAnaloguePosX, moveAnaloguePosY, moveRadius, mainAnalogueColour);
    }

    public void update() {
        updateMoveAnaloguePos();
    }

    private void updateMoveAnaloguePos() {
        /*
        The analogue stick needs to be able to move each frame (circle drawn shape).
        To avoid the actual move stick from exceeding the limits of the outer circle radius(shadow),
        the shadow position x is added by the actual actuator position x times by the outer radius.
        The same occurs for the Y axis. The analogue can then have a constantly updated set position.
         */
        moveAnaloguePosX = (int) (outerAnaloguePosX + setPosX*outerRadius);
        moveAnaloguePosY = (int) (outerAnaloguePosY + setPosY*outerRadius);
    }

    public boolean isTouch(float touchPosX, float touchPosY) {
        controllerCenterToTouchDistance = Misc.getDistanceBetween(outerAnaloguePosX, outerAnaloguePosY, touchPosX, touchPosY);
    /*
        - Code has now been duplicated into Misc class.
        (float) Math.sqrt(
        Math.pow(outerAnaloguePosX - touchPosX, 2)
        Math.pow(outerAnaloguePosY - touchPosY, 2)
        - Prevented movement of one diagonal direction before correcting mistake.
    */
        return controllerCenterToTouchDistance < outerRadius;
    }

    public void setIsTouch(boolean isTouch) {
        this.isTouch = isTouch; // set equal to the input argument "isTouch" above
    }

    public boolean getIsTouch() {
        return isTouch;
    }

    public void setPos(float touchPosX, float touchPosY) {
        /* 0 = analogue stick is in the center,
        1 = fully at edge of outer circle.
        */
        float deltaX = touchPosX - outerAnaloguePosX;
        float deltaY = touchPosY - outerAnaloguePosY;
        float deltaDistance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); // cast to float to convert from double!

        if(deltaDistance < outerRadius) {
            setPosX = deltaX/outerRadius; // pulled analogue stick and divided total distance from the center to the border.
            setPosY = deltaY/outerRadius;
        } else { // If the deltaDistance is larger than the outer circle radius of the analogue stick controller, then...
            setPosX = deltaX/deltaDistance;
            setPosY = deltaY/deltaDistance; // boring vector normalisation... its reverse the above code...
            // same issue before correction of analogue movement prevention of one direction.
        }
    }

    public void resetPos() {
        setPosX = (float) 0.0; // set cast to float to convert from double
        setPosY = (float) 0.0; // set cast to float to convert from double
    }

    public float getSetX() {
        return setPosX;
    }

    public float getSetY() {
        return setPosY;
    }
}
