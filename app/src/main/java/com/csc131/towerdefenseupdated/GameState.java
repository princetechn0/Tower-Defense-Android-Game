package com.csc131.towerdefenseupdated;
import android.content.Context;

import java.util.Random;

final class GameState {
    // This object will have access to the deSpawnReSpawn method
    // in GameEngine- once it is initialized

    // Objects for the game loop/thread
    public Thread mThread = null;
    // Control pausing between updates
    public long mNextFrameTime;

    // Is the game currently playing and or paused?
    public volatile boolean mPlaying = false;
    public volatile boolean mEndofRound = true;
    public volatile boolean mDead = true;
    public volatile boolean mEditing = false;
    public volatile boolean mTowerClicked = false;
    public volatile boolean mBuying = false;




    // The Round Number
    public int mRound;

    // How much currency does the player have
    public int mCurrency;

    // How much health does the SpaceStation have left
    public int mStationHealth;

    // How many Towers exist
    private int mNumTowers;

    // Which Tower user preparing to buy
    public int activeBuyer;

    // Which Tower is Active
    public int activeTower;

    // How many Enemies exist
    private int mNumEnemies;
    // How much health does the Enemy have
    public int mEnemyHealth;

    // How many Enemies Exist of Type
    public int num_enemy1 = 0;
    public int num_enemy2 = 0;
    public int num_enemy3 = 0;


    //Random
    Random random = new Random();

    void startRound() {
        mPlaying = true;
        mEndofRound = false;
        mDead = false;
        mEditing = false;
        mTowerClicked = false;
        mBuying = false;
    }

    void newGame() {
        mPlaying = true;
        mEndofRound = true;
        mDead = true;
        mEditing = false;
        mTowerClicked = false;
        mBuying = false;

    }

    void resetVariables(){
        mRound = 1;

        mCurrency = 650;

        mStationHealth = 40;

        mNumTowers = 0;

        mNumEnemies = 0;

        num_enemy1 = 0;
        num_enemy2 = 0;
        num_enemy3 = 0;
    }

    int getNumTowers(){
        return mNumTowers;
    }

    void incrementNumTowers() {
        mNumTowers++;
    }

    int getNumEnemies(){
        return mNumEnemies;
    }

    void increaseRoundNumber(){
        mRound++;
    }

    void increaseCurrency() {
        mCurrency += random.nextInt(100 + 50);
    }

    int getmStationHealth() {
        return mStationHealth;
    }


    int getRoundNumber(){
        return mRound;
    }

    int getCurrency() {
        return mCurrency;
    }



}


