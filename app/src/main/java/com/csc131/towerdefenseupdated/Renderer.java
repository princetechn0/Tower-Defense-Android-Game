package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

class Renderer {
     Canvas mCanvas;
     SurfaceHolder mSurfaceHolder;
     Paint mPaint;

    // The size in segments of the playable area
    final int NUM_BLOCKS_WIDE = 40;
    int mNumBlocksHigh;
    int blockSize;



    void draw(Context context, GameState gs, HUD hud, SpaceStation sp,
              ArrayList<Enemy> enemyArrayList, ArrayList<Tower1> tower1ArrayList, ExplosionEffectSystem explosionEffectSystem) {

        if (mSurfaceHolder.getSurface().isValid()) {

            mCanvas = mSurfaceHolder.lockCanvas();
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapmap);
            mCanvas.drawBitmap(bmp, 0, 0, mPaint);

            drawSpaceStation(gs, context, sp);


            // Handles the tower that is currently selected
            if(gs.mTowerClicked) {
                tower1ArrayList.get(gs.activeTower).drawEditingArea(mCanvas, mPaint);
            }

            for(Tower1 t: tower1ArrayList) {
                t.draw(mCanvas, mPaint);
            }

            if (gs.mPlaying) {
                // Draw all the game objects here
            }

            //If the game is running in a round, print the enemy objects
            if(!gs.mEndofRound) {
                for (Enemy e: enemyArrayList) {
                    e.draw(mCanvas, mPaint);
                }
                //Disables Start Button
                hud.disableStartButton(mCanvas, mPaint);
            }

            if(gs.mDead) {
                // Draw a background graphic here
            }

            if(gs.mEditing) {
                // Warns User of Where Not to Place Tower
                hud.drawWarningZone(mCanvas, mPaint);
            }



//            Draw a particle system explosion here
            if(explosionEffectSystem.mIsRunning) {
                explosionEffectSystem.draw(mCanvas, mPaint);
            }

            // Now we draw the HUD on top of everything else
            hud.draw(context, mCanvas, mPaint, gs);

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }



    void drawSpaceStation(GameState gs, Context context, SpaceStation sp) {
        if(gs.mStationHealth > 30) {
            sp = new SpaceStation(context, R.drawable.spacestation);
        } else if (gs.mStationHealth <= 30 && gs.mStationHealth > 20) {
            sp = new SpaceStation(context, R.drawable.spacestation2);
        } else if (gs.mStationHealth <= 20 && gs.mStationHealth > 10) {
            sp = new SpaceStation(context, R.drawable.spacestation3);
        } else if (gs.mStationHealth <= 10 && gs.mStationHealth > 0) {
            sp = new SpaceStation(context, R.drawable.spacestation4);
        } else {
            sp = new SpaceStation(context, R.drawable.spacestationdead);
        }
        sp.draw(mCanvas, mPaint);
    }

}
