package com.example.tilebasedshooterscratchet;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View; // view was required for testing in the world.
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer battleOst; // java/res/raw/marcadia.mp3 (no caps is important for file name)
    // private SoundEffects collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // collection = new SoundEffects(this);

        battleOst = MediaPlayer.create(MainActivity.this, R.raw.marcadia);// instead of get.applicationContext
        battleOst.setLooping(true); // music keeps looping.
        battleOst.setVolume(50,50); // volume left and right.
        battleOst.start(); // music plays on start.

        // setups window so it is fullscreen and hide the status bar
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        // content view setup to World so all objects are rendered inside game
        setContentView(new World(this));
    }

    protected void onPause() {
        super.onPause();
        battleOst.pause();
    }

    protected void onDestroy() {
        super.onDestroy();
        battleOst.stop();
        battleOst.release();
    }
}