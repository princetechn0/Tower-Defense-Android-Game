package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

class SoundEngine {
    // for playing sound effects
    private SoundPool mSP;
    private int mEnemyDeath_ID = -1;
    private int mTowerCollision_ID = -1;


    SoundEngine(Context context) {
//        Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("deathsound.mp3");
            mEnemyDeath_ID = mSP.load(descriptor, 0);
            descriptor = assetManager.openFd("stationcollision.mp3");
            mTowerCollision_ID = mSP.load(descriptor, 0);


        } catch (IOException e) {
            // Error
        }
    }


    public void playEnemyDeadAudio() {
        mSP.play(mEnemyDeath_ID, 1, 1, 0, 0, 1);
    }
    public void playStationCollisionAudio() {
        mSP.play(mTowerCollision_ID, 1, 1, 0, 0, 1);
    }


}
