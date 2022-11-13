package com.example.tilebasedshooterscratchet;

public class Misc {

    /**
     * The distance between the four points are measured.
     * @param point1X
     * @param point1Y
     * @param point2X
     * @param point2Y
     * @return
     */

    public static float getDistanceBetween(float point1X, float point1Y, float point2X, float point2Y) {
        return (float) Math.sqrt(
                Math.pow(point1X - point2X, 2) +
                        Math.pow(point1Y - point2Y, 2)
        );
    }
}
