package com.example.tilebasedshooterscratchet;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

// MAIN CLASS ------------------------------------------------------------------------------------
public class WorldLoop extends Thread { // threads are used in processors to run in parallel.

    //IMPORTANT - THE UPDATE COUNT LIMITER BELOW
    public static final double MAX_UPDATE = 60.0; // keep at a whole number!
    /*
     Keep at whole number of possible issues after rounding with floats later on.
     Changed to public to allow Player class to access.
     Limiter - larger can reduce rendering speed and the main game loop.
    */

    private static final double UPDATE_PERIOD = 1E+3/MAX_UPDATE;
    /*
     these final constants cannot change during run time.
     1E+3 is 1000.
    */

    private World world;
    private boolean isActive = false; // private parameter that states if the game is running.
    private SurfaceHolder surfaceHolder; // field required for locking the canvas etc.
    private double averageUpdate; // required for calculations of actual value here.
    private double averageFrames; // required for calculations of actual value here.


    // initialise all created fields below into this surfaceHolder constructor.
    public WorldLoop(World world, SurfaceHolder surfaceHolder) {
        this.world = world;
        this.surfaceHolder = surfaceHolder; // initialise in surfaceHolder constructor.
    }

    public double getAverageUpdate() {
        return averageUpdate;
    }

    public double getAverageFrames() {
        return averageFrames;
    }

    public String getDrawCredentials() {
        return "Created by Ben Hesketh, 18008836";
    }

    public void startLoop() {
        isActive = true;
        start(); // the start function already exists in the Thread class.
    }

    // ctrl + shift + a to add new methods
    @Override
    public void run() {
        super.run();

        // logic for calculating time and cycle count
        int updateCycleCount = 0;
        int frameCycleCount = 0;

        //long variables measured in milliseconds
        long startTime;
        long totalRunTime;
        long sleepyTime;

        // MAIN GAME LOOP ------------------------------------------------------------------------
        Canvas canvas;
        startTime = System.currentTimeMillis();
        while (isActive) {
            // Rendering and updating
            try {
                canvas = surfaceHolder.lockCanvas(); // locks the canvas and returns canvas object.
                //synchronized (surfaceHolder) {
                    /* synchronized block prevents multiple threads from calling
                     the update and draw methods. May not be needed. */
                world.update();
                updateCycleCount++;
                world.draw(canvas);
                //}
                surfaceHolder.unlockCanvasAndPost(canvas);
                frameCycleCount++;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            /*
             Pause game loop when update per second target is about to be exceeded
             having no target can cause rendering to slow down if there is too
             much to update in one period.
             UPDATE_PERIOD is the inverse of the target update frequency, to tell how much
             time should have passed.

             Goal for sleep function - The amount of time needed for the thread to sleep to match
             the final update count is known, possible by subtracting the run time so far.
            */
            totalRunTime = System.currentTimeMillis() - startTime;
            sleepyTime = (long) (updateCycleCount  * UPDATE_PERIOD - totalRunTime);
            if (sleepyTime > 0) {
                try {
                    // thread goes to sleep for duration of variable "sleepyTime".
                    sleep(sleepyTime); // this is a java.lang.thread function.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    /* auto generated with alt + enter to catch invalid exception
                    since sleep time starts at 0. */
                }
            }

            /*
             Skip frames to keep on track with update per second target (consistency)
             This will reduce the frame count, preventing the FPS from being forced
             to match the Update count. UpdateCycleCount must be below 59.00 instead of 60 just in
             case.
            */
            while(sleepyTime < 0 && updateCycleCount < MAX_UPDATE-1) { // while thread is not ASLEEP
                world.update();
                updateCycleCount++;
                totalRunTime = System.currentTimeMillis() - startTime;
                sleepyTime = (long) (updateCycleCount  * UPDATE_PERIOD - totalRunTime);
            }

            // Calculations of update per second and frames per second
            totalRunTime = System.currentTimeMillis() - startTime;
            if(totalRunTime >= 1000) { // start of 1 second interval
                averageUpdate = updateCycleCount / (1E-3 * totalRunTime); // 1E-3 instead of 1000
                averageFrames = frameCycleCount / (1E-3 * totalRunTime); // - due to long variable.
                updateCycleCount = 0;
                frameCycleCount = 0;
                startTime = System.currentTimeMillis();
            }
            // reset after 1 second!
        }
        // END OF MAIN GAME LOOP -----------------------------------------------------------------
    }
}
