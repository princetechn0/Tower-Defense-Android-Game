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
              Enemy enemy, ArrayList<Enemy> enemyArrayList, ExplosionEffectSystem explosionEffectSystem) {

        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapmap);
            mCanvas.drawBitmap(bmp, 0, 0, mPaint);

            sp = new SpaceStation(context);
            sp.draw(mCanvas, mPaint);

//            enemy.draw(mCanvas, mPaint);

            for (int i = 0; i <enemyArrayList.size(); i++) {
                enemyArrayList.get(i).draw(mCanvas, mPaint);
            }


            if (gs.mPlaying) {
                // Draw all the game objects here

            }

            if(gs.mDead) {
                // Draw a background graphic here
            }

//             Draw a particle system explosion here
            if(explosionEffectSystem.mIsRunning) {
                explosionEffectSystem.draw(mCanvas, mPaint);
            }

            // Now we draw the HUD on top of everything else
            hud.draw(context, mCanvas, mPaint, gs);

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
