package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

public class TowerLaser {

        // The location in the grid of all the segments
        private ArrayList<TPoint> segmentLocations;

        private Bitmap mBitMaps[] = new Bitmap[1];

        // Bounding Rect
        Rect boundingRect;


        TowerLaser(Context context, String kind) {

            // Initialize our ArrayList
            segmentLocations = new ArrayList<>();

            int rDrawable;
            switch (kind) {
                case "tower3":
                    rDrawable = R.drawable.lasergreen;
                    break;
                case "tower2":
                    rDrawable = R.drawable.laserred;
                    break;
                case "tower1":
                default:
                    rDrawable = R.drawable.laserblack;
                    break;
            }


            mBitMaps[0] = BitmapFactory
                    .decodeResource(context.getResources(),
                            rDrawable);
            bitmapInit();

            segmentLocations.add(new TPoint(-500, -500));

            boundingRect = new Rect();
        }


        // sets laser at tower head
        void reset(int x, int y) {
            // Delete the old contents of the ArrayList
            segmentLocations.clear();

            segmentLocations.add(new TPoint(x-50, y-10));

            // For detecting collision
            boundingRect = new Rect(segmentLocations.get(0).point.x - 10 ,segmentLocations.get(0).point.y - 10,
                    segmentLocations.get(0).point.x + 10, segmentLocations.get(0).point.y + 10);
        }




        void draw(Canvas canvas, Paint paint) {
            if (!segmentLocations.isEmpty()) {
                canvas.drawBitmap(mBitMaps[0] ,
                        segmentLocations.get(0).point.x,
                        segmentLocations.get(0).point.y, paint);


//                Debugging
//                paint.setColor(Color.argb(50,255,255,255));
//                canvas.drawRect(boundingRect, paint);
//                paint.setColor(Color.argb(255,255,255,255));

            }

        }


        void resetLaserOffscreen() {
            segmentLocations.clear();

            segmentLocations.add(new TPoint(-500, -500));
            updateBoundingRect(new TPoint(-500, -500));

        }


        Point laserLocation() {
            return segmentLocations.get(0).point;
        }


    void move(double deltaX, double deltaY) {
            // Move the head in the appropriate heading
            // Get the existing head position
            TPoint p = segmentLocations.get(0);

            //testing values
            p.point.y += deltaY;
            p.point.x += deltaX;

            updateBoundingRect(p);

        }

    void updateBoundingRect(TPoint p) {
        boundingRect.set(p.point.x, p.point.y, p.point.x + 40, p.point.y + 40);
    }



        void bitmapInit() {
            mBitMaps[0] = Bitmap
                    .createScaledBitmap(mBitMaps[0],
                            50, 50, false);
            Matrix matrix = new Matrix();
            mBitMaps[0]  = Bitmap.createBitmap(mBitMaps[0],
                    0, 0, 50, 50, matrix, true);

        }
    }
