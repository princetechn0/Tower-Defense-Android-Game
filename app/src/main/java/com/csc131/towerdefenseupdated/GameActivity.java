package com.csc131.towerdefenseupdated;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();
        TPoint size = new TPoint();

        display.getSize(size.point);

        // Create a new instance of the GameEngine class
        gameEngine = new GameEngine(this, size);

        // Make GameEngine the view of the Activity
        setContentView(gameEngine);


    }


    // Start the thread in GameEngine
    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }

    // Stop the thread in GameEngine
    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.pause();
    }

}
