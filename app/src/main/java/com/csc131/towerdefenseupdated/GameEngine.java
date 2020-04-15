package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Handler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;


class GameEngine extends SurfaceView implements Runnable, HUDBroadcaster {

    // Game State Object
    public GameState gameState = new GameState();

    // Towers
    ArrayList<Tower1> tower1ArrayList = new ArrayList<>();

    // Game Objects
    SpaceStation spaceStation;
    ArrayList<Enemy> enemyArrayList = new ArrayList<>();


    // Enemy Spawn
     Handler handler = new Handler();
     Boolean isActive;


    // Audio Related
    private SoundEngine audioEngine = new SoundEngine(getContext());

    // Drawing
    HUD mHUD;
    Renderer mRenderer;
    ExplosionEffectSystem explosionEffectSystem;
    PhysicsEngine physicsEngine;

    // Input Observer
    private ArrayList<InputObserver> inputObservers = new ArrayList();
    HUDController mHudController;


    // Creating Level
    Level level;


    // Creating on Screen Messages
    com.csc131.towerdefenseupdated.Toast toast = new com.csc131.towerdefenseupdated.Toast(getContext());

    public GameEngine(Context context, TPoint size) {
        super(context);

        mRenderer = new Renderer();
        screenResolutionInit(size);

        mHudController = new HUDController(this, this);

        mHUD = new HUD(size);

        spaceStation = new SpaceStation(context, R.drawable.spacestation);

        physicsEngine = new PhysicsEngine();
        explosionEffectSystem = new ExplosionEffectSystem();
        explosionEffectSystem.init(50);

        level = new Level(getContext(), mRenderer, enemyArrayList, gameState.num_enemy1, gameState.num_enemy2, gameState.num_enemy3);

    }




    // Called to start a new game
    public void newGame() {
        toast.onScreenMessages("Welcome to Space Tower Defense!");

        // Handler
        handlerReset();

        // Reset the enemies off screen and face them in the right direction
        resetEnemies();

        tower1ArrayList.clear();

        spaceStation.spawn();

        // Reset Score and Everything
        gameState.resetVariables();

        // Setup mNextFrameTime so an update can triggered
        gameState.mNextFrameTime = System.currentTimeMillis();
    }




    // Called to start a new game
    public void nextRound() {
        toast.onScreenMessages("Next Round Beginning!");

        handlerReset();

        level.clear();

        level.enemyIncrementer(gameState);

        level = new Level(getContext(), mRenderer, enemyArrayList, gameState.num_enemy1, gameState.num_enemy2, gameState.num_enemy3);

        // Reset the enemies offscreen
        resetEnemies();

        // Reset towers
        resetTowers();

        // Setup mNextFrameTime so an update can triggered
        gameState.mNextFrameTime = System.currentTimeMillis();

    }

    void resetEnemies() {
        for (Enemy e: enemyArrayList) {
            e.reset();
            e.beginMoving();
        }
    }

    void resetTowers() {
        for (Tower1 t: tower1ArrayList) {
            t.resetDirection();
        }
    }



    // Handles the game loop
    @Override
    public void run() {
        newGame();

        while (gameState.mPlaying) {
            // This call to update will evolve with the project
            if(physicsEngine.update(5, explosionEffectSystem)){}


            if (!gameState.mDead && !gameState.mEndofRound) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }
            mRenderer.draw(getContext(), gameState, mHUD, enemyArrayList, tower1ArrayList, explosionEffectSystem);
        }


    }

    void handlerReset() {
        isActive = false;
        handler.removeCallbacksAndMessages(null);
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 30;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(gameState.mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            gameState.mNextFrameTime = System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }



    // Update all the game objects
    public void update() {

        //Prints all the enemies with a time delay, starting with the First and a three second delay
        handleTime(0, 3000);

        for (Tower1 t : tower1ArrayList) {
            for (Enemy e : enemyArrayList) {
                if (t.pointInCircle(e.enemyLocation(), t.towerLocation(), t.radius)) {

                    // If the enemy has not been added to the arraylist yet, add it and save its location
                    if (!t.enemiesInACircle.contains(e)) {
                        e.distFromTower = (int) Math.hypot(e.enemyLocation().x - t.towerLocation().x, e.enemyLocation().y - t.towerLocation().y);
                        t.enemiesInACircle.add(e);
                    }

                } else {

                    if (t.enemiesInACircle.size() != 0) {
                        t.enemiesInACircle.remove(e);
                    }
                    else {
                        t.resetDirection();
                        t.hideLasers();
                        t.enemyToFollow = 0;
                    }
                }

            }

            // of all enemies within the zone, find the one with the shortest distance to the tower, then follow it
            for (Enemy x : t.enemiesInACircle) {
                t.distFromTower.add(x.distFromTower);

                // Finds enemy with Shortest Distance to tower and sets it to be attacked
                if (Collections.min(t.distFromTower) == x.distFromTower) {
                    t.enemyToFollow = t.enemiesInACircle.indexOf(x);
                }

                // rotate towards it
                t.rotateTower(t.enemiesInACircle.get(t.enemyToFollow).enemyLocation());

                gameState.mFire = true;
                t.updateLaser(t.enemiesInACircle.get(t.enemyToFollow).enemyLocation());

                // Check if enemy and laser bounding rects intersect, then remove it offscreen
                if(x.detectDeath(audioEngine, explosionEffectSystem, t.towerLaser.boundingRect)) {
                    t.enemiesInACircle.remove(x);
                    enemyArrayList.remove(x);
                }
            }

        }

//        for(Enemy z: enemyArrayList) {
//            if(z.detectHittingSpaceStation(gameState, audioEngine, explosionEffectSystem, spaceStation.boundingRect)){
//                enemyArrayList.remove(z);
//            }
//        }

        if(enemyArrayList.isEmpty()) {
            // Increment Game Round Number and increase Currency
            gameState.increaseRoundNumber();
            gameState.increaseCurrency();

            // Pause the game ready to start again
            gameState.mDead = true;
            gameState.mEndofRound = true;
            gameState.mFire = false;

            toast.onScreenMessages("Round " + gameState.mRound + " Complete!" +
                    "\n Money Made: $" + gameState.currencyDifference);
        }

    }


    //Prints all the enemies with a time delay
    void handleTime(final int enemyNumber,  int delay) {
        isActive = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isActive){
                    if(enemyNumber < enemyArrayList.size()){
                        enemyArrayList.get(enemyNumber).move();
                        handleTime(enemyNumber+1, 5000);
                    }
                }
            }
        }, delay);

    }


    void screenResolutionInit(TPoint size) {
        // Work out how many pixels each block is
        mRenderer.blockSize = size.point.x / mRenderer.NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mRenderer.mNumBlocksHigh = size.point.y / mRenderer.blockSize;

        // Initialize the drawing objects
        mRenderer.mSurfaceHolder = getHolder();
        mRenderer.mPaint = new Paint();
    }


    // For the game engine broadcaster interface
    public void addObserver(InputObserver o) {
        inputObservers.add(o);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//         Handle the player's input here
//         But in a new way
        for (InputObserver o : inputObservers) {
            o.handleInput(getContext(), mRenderer,  motionEvent, gameState, mHUD.getOffLimitAreas(), mHUD.getControls(),
                    mHUD.getExtensiveControls(), tower1ArrayList, toast);
        }

        return true;
    }


    // Stop the thread
    public void pause() {
        gameState.mPlaying = false;
        try {
            gameState.mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        gameState.mPlaying = true;
        gameState.mThread = new Thread(this);
        gameState.mThread.start();
    }
}
