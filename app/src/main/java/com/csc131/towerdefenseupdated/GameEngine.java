package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Handler;

import java.util.ArrayList;

class GameEngine extends SurfaceView implements Runnable, HUDBroadcaster {

    //Game State Object
    private GameState gameState = new GameState();

    //Game Objects
    SpaceStation spaceStation;
    ArrayList<Enemy> enemyArrayList = new ArrayList<>();

    //Times Enemy Spawn
    final Handler handler = new Handler();


    //Audio Related
    private SoundEngine audioEngine = new SoundEngine(getContext());

    //Drawing
    HUD mHUD;
    Renderer mRenderer;
    ExplosionEffectSystem explosionEffectSystem;
    PhysicsEngine physicsEngine;

    //Input Observer
    private ArrayList<InputObserver> inputObservers = new ArrayList();
    HUDController mHudController;


    //Testing Level Constructor
    Level level1;


    public GameEngine(Context context, TPoint size) {
        super(context);

        mRenderer = new Renderer();
        screenResolutionInit(size);

        mHudController = new HUDController(this, this);

        mHUD = new HUD(size);

        spaceStation = new SpaceStation(context);

        physicsEngine = new PhysicsEngine();

        explosionEffectSystem = new ExplosionEffectSystem();
        explosionEffectSystem.init(100);

        //Creates a new Level with Corresponding Enemy Numbers
        level1 = new Level(mRenderer, enemyArrayList, 5, 2, 1);
        level1.levelCreator(context);



    }

    // Called to start a new game
    public void newGame() {

        // reset the enemies off screen and face them in the right direction
        resetEnemies();

        spaceStation.spawn();

        //Reset Score and Everything
        gameState.resetVariables();

        // Setup mNextFrameTime so an update can triggered
        gameState.mNextFrameTime = System.currentTimeMillis();
    }

    void resetEnemies() {
        for (Enemy e: enemyArrayList) {
            e.reset();
            e.beginMoving();
        }
    }


    // Called to start a new game
    public void nextRound() {

        // reset the enemies offscreen
        resetEnemies();

        // Setup mNextFrameTime so an update can triggered
        gameState.mNextFrameTime = System.currentTimeMillis();
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
            mRenderer.draw(getContext(), gameState, mHUD, spaceStation, enemyArrayList, explosionEffectSystem);
        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
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


    //Prints all the enemies with a time delay
    void handleTime(final int enemyNumber, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(enemyNumber < enemyArrayList.size()){
                        enemyArrayList.get(enemyNumber).move();
                        handleTime(enemyNumber+1, 1000);
                    }
            }
        }, delay);
    }


    // Update all the game objects
    public void update() {

        //Prints all the enemies with a time delay, starting with the First and a three second delay
        handleTime(0, 3000);


        // Did the Alien die?
        for (Enemy e: enemyArrayList) {
            if (e.detectDeath()) {
                //Death Audio
                audioEngine.playEnemyDeadAudio();

            //If last enemy dies, change state
            if(enemyArrayList.get(enemyArrayList.size()-1).detectDeath()){
                // Pause the game ready to start again
                gameState.mDead = true;
                gameState.mEndofRound = true;
            }

                // Emits a particle system effect when the alien reaches the Space Station
                explosionEffectSystem.emitParticles(new PointF(1600,500));

                //Resets the enemy to the original position off screen, ready for next wave
                e.reset();

                gameState.mStationHealth+= e.alienDamageAmount;

            }
        }




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
            o.handleInput(motionEvent, gameState, mHUD.getControls());
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
