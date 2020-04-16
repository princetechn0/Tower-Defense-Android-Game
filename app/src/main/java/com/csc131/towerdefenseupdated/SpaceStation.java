package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpaceStation {
    private TPoint location = new TPoint();

    // An image to represent the spacestation
    private Bitmap mBitmapSpaceStation;

    // Bounding Rect
    Rect boundingRect;

    /// Set up the SpaceStation in the constructor
    public SpaceStation(Context context, int x){
        this.mBitmapSpaceStation = BitmapFactory.decodeResource(context.getResources(), x);

        boundingRect = new Rect();

    }


    // This is called whenever the game is created
    void spawn(){
        // Place SpaceStation at this location
        location.point.x = 1430;
        location.point.y = 250;

        boundingRect.set(1520, 400, 1680, 580);
    }


    // Draw the station
    void draw(Canvas canvas, Paint paint){
        spawn();
        mBitmapSpaceStation = Bitmap.createScaledBitmap(mBitmapSpaceStation, 350, 350, true);
        canvas.drawBitmap(mBitmapSpaceStation,
                location.point.x , location.point.y  , paint);


//        Debugging
//        paint.setColor(Color.argb(50,255,255,255));
//        canvas.drawRect(boundingRect, paint);
//        paint.setColor(Color.argb(255,255,255,255));
    }


}
