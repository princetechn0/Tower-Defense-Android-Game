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



    void draw(Context context, GameState gs, HUD hud,
              ArrayList<Enemy> enemyArrayList, ArrayList<Tower1> tower1ArrayList,
              ExplosionEffectSystem explosionEffectSystem) {

        if (mSurfaceHolder.getSurface().isValid()) {

            // As long as Game is running
            if (gs.mPlaying) {
                // Draw all the game objects here
                mCanvas = mSurfaceHolder.lockCanvas();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapmap);
                mCanvas.drawBitmap(bmp, 0, 0, mPaint);

                drawSpaceStation(gs, context);

                for(Tower1 t: tower1ArrayList) {
                    t.draw(mCanvas, mPaint);
                }
            }


            //If the game is running in a round, draw the enemies, as well as towers
            if(!gs.mEndofRound) {

                for (Enemy e: enemyArrayList) {
                    e.draw(mCanvas, mPaint);
                }

                for(Tower1 t: tower1ArrayList) {
                    t.draw(mCanvas, mPaint);

                    if(gs.mFire) {
                        t.towerLaser.draw(mCanvas, mPaint);
                    }

                }

                //Disables Start Button
                hud.disableStartButton(mCanvas, mPaint);
            }


            if(gs.mEditing) {
                // Warns User of Where Not to Place Tower
                hud.drawWarningZone(mCanvas, mPaint);
            }

            // Handles when a user goes to buy a tower
            if(gs.mBuying) {
                hud.drawBuyingControls(mCanvas, mPaint, gs);
            }

            // Handles the tower that is currently selected
            if(gs.mTowerClicked) {
                tower1ArrayList.get(gs.activeTower).drawRadius(mCanvas, mPaint);
                hud.drawExtensiveControls(mCanvas, mPaint, gs, tower1ArrayList);
            }


            // Draw a particle system explosion here
            if(explosionEffectSystem.mIsRunning) {
                explosionEffectSystem.draw(mCanvas, mPaint);
            }



                // Now we draw the HUD on top of everything else
            hud.draw(context, mCanvas, mPaint, gs);

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }



    void drawSpaceStation(GameState gs, Context context) {
        SpaceStation sp;
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
