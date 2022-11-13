package com.example.tilebasedshooterscratchet;

import android.content.Context; // Import required for constructor argument.
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
// import android.media.AudioManager; - Import obsolete now with new AudioAttributes library.
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder; // Import required for super class.
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList; // Used for containing numerous Reds and Projectiles from weapon.
import java.util.Iterator;
import java.util.List;

// super class renamed to SurfaceView and imported all methods.
public class World extends SurfaceView implements SurfaceHolder.Callback {
    private final Controller controller; // - Links Controller Class
    private final Player player; // - Links Player Class
    private final Boss boss; // Links Boss Class
    private final Boss boss1; // Links Boss Class
    private final Medic medic; // Links Medic Class

    /** Sound effects initialising */
    private AudioAttributes audioAttributes; // Allows sound effects to
    final int SOUND_POOL_MAX = 5;
    protected static SoundPool collection;

    private static int damageSound;
    private static int gameOver;
    private static int blast;
    private static int heal;
    private static int shoot;

    /** ------------------------------- */

    /** Array Lists for Projectiles and Reds. */

    private List<Reds> redsList = new ArrayList<Reds>();
    private List<Fire> fireList = new ArrayList<Fire>();

    /** ------------------------------- */

    private WorldLoop worldLoop; // Private object is called WorldLoop which is the same name but with a capital at start.
    private Context context;  // Created field called context within this game.
    private Background background;

    /** Music */
    MediaPlayer battleOst; // Main battle theme music from Ratchet & Clank: 3.

    private int numberOfShots = 0;

    /** Booleans to prevent damage and healing spam from Medic and Boss */
    private boolean struck = false;
    private boolean healed = false;
    private EndGame endGame;

    public World(Context context) { //constructor argument type changed
        super(context); // called constructor of the super class

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this); // "this" refers to the instance of the class.
        // This all allows objects to be rendered onto the screen.
        this.context = context;
        // Initialising context.
        worldLoop = new WorldLoop(this, surfaceHolder);

        /** SOUND EFFECTS
         * ---------------------------------------------------------------------- */

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

        // SoundPool is no longer used after a certain API.
        // SoundPool (int maxStreams, int streamType, int srcQuality) - Discontinued.
        collection = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(SOUND_POOL_MAX)
                .build();

        damageSound = collection.load(context, R.raw.hit, 1);
        gameOver = collection.load(context, R.raw.gameover, 1);
        blast = collection.load(context, R.raw.fire, 1);
        heal = collection.load(context, R.raw.heal, 1);
        shoot = collection.load(context, R.raw.shoot, 1);

        /** --------------------------------------------------------------------- */

        /*
         Pressed alt + enter to create a new class called WorldLoop for main game loop.
         The WorldLoop object is removed before class since it is mentioned above now as private.
         */

        /**
         Initialising of the Player and other objects.
         ----------------------------
         Requires constructor arguments for the player to allow sprites to work.
        */

        controller = new Controller(270, 350, 70, 40);
        player = new Player(getContext(), controller, 300, 30, 50, getResources());

        /** End Game Text Panel on top of background when Player loses all Hp. */
        endGame = new EndGame(getContext());

        /** Background for World (inspired by Ratchet a& Clank: 3 Planet Marcadia */
        background = new Background(getContext(), 10, 10, getResources());

        /** 2 Bosses */
        boss = new Boss(getContext(), player, 570, 670, 50, getResources());
        boss1 = new Boss(getContext(), player, 1070, 270, 50, getResources());

        /** 1 Medic */
        medic = new Medic(getContext(), player, 1000, 800, 50, getResources());

        setFocusable(true);
    }

    /** SOUND EFFECTS METHODS
     * ---------------------------------------------------------------------- */
    public void playHitSound() {
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        collection.play(damageSound, 1, 1, 1, 0, 1);
    }

    public void playBlastSound() {
        collection.play(blast, 1, 1, 1, 0, 1);
    }

    public void playHealSound() {
        collection.play(heal, 1, 1, 1, 0, 1);
    }

    public void playShootSound() {
        collection.play(shoot, 1, 1, 1, 0, 1);
    }

    public void playGameOverSound() {
        collection.play(gameOver, 1, 1, 1, 0, 1);
    }

    /** --------------------------------------------------------------------- */


    /** CONTROLS
     * ---------------------------------------------------------------------- */
    @Override
    public boolean onTouchEvent(MotionEvent event) { // I accidentally chose the security version of this method.
        // required for actions by touching the screen
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // Press down onto the screen.
                if (controller.getIsTouch()) {
                    numberOfShots++; // Increments number of projectiles in the scene from the player.
                    playBlastSound();
                } else if (controller.isTouch(event.getX(), event.getY())) {
                    controller.setIsTouch(true);
                } else {
                    fireList.add(new Fire(getContext(), player));
                    playBlastSound();
                }
                return true;
            case MotionEvent.ACTION_MOVE: // Android Studio merged cases together before and works!
                if (controller.getIsTouch()) {
                    controller.setPos(event.getX(), event.getY());
                }
                //player.setPosition((float) event.getX(), (float)event.getY()); // converted from int to float.
                return true; // indicates that the event has been handled.

            case MotionEvent.ACTION_UP:
                controller.setIsTouch(false);
                controller.resetPos();
                return true;
        }
        return super.onTouchEvent(event);
    }
    /** --------------------------------------------------------------------- */

    /*
    Allows control of the surface size and format, edit the pixels in the surface, and
     monitor changes to the surface.
    */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        worldLoop.startLoop(); // Method belongs in WorldLoop Class.
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    // override draw method for initial feedback on rendering
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //below methods need to be declared here with class "canvas"
        drawCredentials(canvas);
        drawFrames(canvas);
        drawUpdate(canvas);
        background.draw(canvas);
        controller.draw(canvas);
        player.draw(canvas); // draw method for player found in Player.java
        boss.draw(canvas);
        boss1.draw(canvas);
        medic.draw(canvas);
        //reds.draw(canvas); TEST - draw method for reds found in Reds.java (overridden).
        background.setPos(canvas);
        background.setSize(canvas);
        for (Reds reds : redsList) {
            reds.draw(canvas);
        }
        for (Fire fire : fireList) {
            fire.draw(canvas);
        }

        /** GAME OVER WHEN PLAYER'S HEALTH DEPLETES TO ZERO. */
        if (player.getHealth() <= 0) {
            // playGameOverSound(); - Removed because it repeated infinitely.
            endGame.draw(canvas);
        }
    }

    public void drawCredentials(Canvas canvas) {
        String creatorName = worldLoop.getDrawCredentials(); // Remains as a string.
        Paint paint = new Paint();
        int colourTextSplinter = ContextCompat.getColor(context, R.color.splinterGreen);
        paint.setColor(colourTextSplinter); // Splinter Cell!
        paint.setTextSize(40);
        // Text is returned in WorldLoop Class.
        canvas.drawText(creatorName, 90, 30, paint);
    }

    // both below methods go into WorldLoop.java
    public void drawUpdate(Canvas canvas) {
        String averageUpdate = Double.toString(worldLoop.getAverageUpdate()); // Converts to string.
        // Parameter at the top for WorldLoop
        Paint paint = new Paint();
        // paint is an imported library (android.graphics)
        int colourTextAqua = ContextCompat.getColor(context, R.color.uselessAqua); // KONOSUBA!
        paint.setColor(colourTextAqua); // colourTextAqua is now used (uselessAqua)
        paint.setTextSize(40);
        /** The current updates per second can no longer be seen anyways. */
        //canvas.drawText("Update per second: " + averageUpdate, 90, 70, paint);
    }

    // modified draw method to update for each frame
    public void drawFrames(Canvas canvas) {
        String averageFrames = Double.toString(worldLoop.getAverageFrames()); // converts to string
        // Parameter is at the top for WorldLoop.
        Paint paint = new Paint();
        // paint is an imported library (android.graphics)
        int colourTextScud = ContextCompat.getColor(context, R.color.scuderiaRed); // F1 reference!
        paint.setColor(colourTextScud);
        paint.setTextSize(40);
        /** The current frames per second can no longer be seen anyways. */
        //canvas.drawText("Frames per second: " + averageFrames, 90, 110, paint);
    }

    public void update() {
        /**
         MAIN UPDATE OF WORLD
         -------------------------
        */

        /** Break Loop completely when HP depletes to 0 so the player can not be controlled.
         */
        if (player.getHealth() <= 0) {
            playGameOverSound();
            return;
        }

        player.update(); // Update method has also been added to Player Class.
        controller.update();
        boss.update();
        boss1.update();
        medic.update();
        // reds.update(); - Moved into for loop for redsList below.
        // background.update(); - Background is currently static anyways.

        /** Emits new Red projectiles when it is time. */
        if (Reds.readyToEmit()) {
            redsList.add(new Reds(getContext(), player));
            playShootSound();
        }

        /** Updates each and every Red team member. */
        while (numberOfShots > 0) {
            fireList.add(new Fire(getContext(), player));
            numberOfShots--; // Limits number of shots per update to prevent a crash.
        }
        for (Reds reds : redsList) {
            reds.update();
        }

        // Updates each and every Red team member.
        for (Fire fire : fireList) {
            fire.update();
        }

        /**
         * Prevent boss from depleting player's health immediately.
          */

        if (struck == false) {
            if (Character.isHitting(boss, player)) {
                playHitSound(); // Plays sound effect when taking damage.
                player.setHp((int) player.getHealth() - 20); // Player loses 20% of health.
                struck = true;
            }
        }
        if (!Character.isHitting(boss, player) && (!Character.isHitting(boss1, player))) {
            struck = false;
        }

        if (struck == false) {
            if (Character.isHitting(boss1, player)) {
                playHitSound(); // Plays sound effect when taking damage.
                player.setHp((int) player.getHealth() - 20); // Player loses 20% of health.
                struck = true;
            }
        }
        if (!Character.isHitting(boss1, player) && (!Character.isHitting(boss, player))) {
            struck = false;
        }

        /**
         * Prevent medic from completely healing player's health immediately.
         */

        if (healed == false) {
            if (Character.isHitting(medic, player)) {
                playHealSound(); // Plays sound effect when taking damage.
                player.setHp((int) player.getHealth() + 20); // Player gains 20% of health.
                healed = true;
            }
        }

        if (!Character.isHitting(medic, player)) {
            healed = false;
        }


        /**
         * ITERATORS
         * ==========================================================================
         Delete Reds after collision with the Player.
         Delete Projectiles after collision with Reds.
         Iterators remove objects from a list by traversing it.
         */

        // While there is another Red available in the World...
        Iterator<Reds> iteratorReds = redsList.iterator();
        while (iteratorReds.hasNext()) {
            Character reds = iteratorReds.next();
            if (Character.isHitting(reds, player)) { // Missed colon removed them and caused NullException.
                // Delete the current Red after striking the player.
                iteratorReds.remove();
                playHitSound(); // Plays sound effect when taking damage.
                player.setHp((int)player.getHealth() - 20); // Player loses 20% of health.
                continue; // No longer needs to check if other Reds collide with the player.
            }

            Iterator<Fire> iteratorFire = fireList.iterator();
            while (iteratorFire.hasNext()) {
                Character fire = iteratorFire.next(); // Increments the iterator counter.
                // Delete projectile when it collides with a Red.
                if (Character.isHitting(fire, reds)) {
                    iteratorFire.remove();
                    iteratorReds.remove(); // Kills both the projectile and Red.
                    break; // No longer needs to check if the Red is colliding with another projectile.
                }
            }
        }
        /** --------------------------------------------------------------------- */
    }
}
