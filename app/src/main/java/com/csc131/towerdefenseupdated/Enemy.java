package com.csc131.towerdefenseupdated;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

class Enemy {

    // The location in the grid of all the segments
    private ArrayList<TPoint> segmentLocations;

    // How big is the entire grid
    private TPoint mMoveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
//    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    //right-0, left-1, up-2, down-3
    private Bitmap mBitMaps[] = new Bitmap[4];

    //Alien Damage Amount
    public int alienDamageAmount;


    Enemy(Context context, TPoint mr, String kind) {

        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

        // Initialize the segment size and movement
        // range from the passed in parameters
        mMoveRange = mr;


        int rDrawable;
        switch (kind) {
            case "alien2":
                rDrawable = R.drawable.alien2;
                alienDamageAmount = -2;
                break;
            case "alien3":
                rDrawable = R.drawable.alien3;
                alienDamageAmount = -3;
                break;
            case "alien1":
            default:
                rDrawable = R.drawable.alien1;
                alienDamageAmount = -1;
                break;
        }

        for(int i = 0; i < mBitMaps.length; i++){
            mBitMaps[i] = BitmapFactory
                    .decodeResource(context.getResources(),
                            rDrawable);
        }

        // Modify the bitmaps to face the head
        // in the correct direction
        mBitMaps[0]  = Bitmap
                .createScaledBitmap(mBitMaps[0] ,
                        60, 60, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        createBitmap(1, matrix);
        matrix.preRotate(-90);
        createBitmap(2, matrix);
        matrix.preRotate(180);
        createBitmap(3, matrix);


        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
//        halfWayPoint = mr.point.x  / 2;
    }

    // Get the enemy ready for a new game
    void reset() {
        // Reset the heading
        heading = Heading.UP;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single alien on the left side of the screen, entering the path
        segmentLocations.add(new TPoint(-100,470));

    }


    void move() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
//        for (int i = segmentLocations.size() - 1; i > 0; i--) {
//
//            // Make it the same value as the next segment
//            // going forwards towards the head
//            segmentLocations.get(i).point.x = segmentLocations.get(i - 1).point.x;
//            segmentLocations.get(i).point.y = segmentLocations.get(i - 1).point.y;
//        }

        // Move the head in the appropriate heading
        // Get the existing head position
        TPoint p = segmentLocations.get(0);

        hitsTheCorner();

        // Move it appropriately
        switch (heading) {
            case UP:
                p.point.y-=20;
                break;

            case RIGHT:
                p.point.x+=20;
                break;

            case DOWN:
                p.point.y+=20;
                break;

            case LEFT:
                p.point.x-=20;
                break;
        }

    }



    void draw(Canvas canvas, Paint paint) {

        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            switch (heading) {
                case RIGHT:
                    drawBitmap(0 ,canvas, paint);
                    break;
                case LEFT:
                    drawBitmap(1 ,canvas, paint);
                    break;
                case UP:
                    drawBitmap(2 ,canvas, paint);
                    break;
                case DOWN:
                    drawBitmap(3 ,canvas, paint);
                    break;
            }

        }
    }


    //Causes Alien to Follow the Path
    void hitsTheCorner() {

       if((segmentLocations.get(0).point.equals(360,470))) {
            rightDirections(Heading.LEFT);
       }else if(((segmentLocations.get(0).point.equals(360, 190)))){
           rightDirections(Heading.UP);
       } else if(((segmentLocations.get(0).point.equals(780,190)))){
           rightDirections(Heading.RIGHT);
       } else if(((segmentLocations.get(0).point.equals(780,630)))){
           rightDirections(Heading.DOWN);
       }else if(((segmentLocations.get(0).point.equals(400,630)))){
           rightDirections(Heading.RIGHT);
       } else if(((segmentLocations.get(0).point.equals(400,850)))){
           rightDirections(Heading.UP);
       } else if(((segmentLocations.get(0).point.equals(1180,850)))){
           rightDirections(Heading.LEFT);
       } else if(((segmentLocations.get(0).point.equals(1180,670)))){
           rightDirections(Heading.DOWN);
       } else if(((segmentLocations.get(0).point.equals(960,670)))){
           rightDirections(Heading.LEFT);
       } else if(((segmentLocations.get(0).point.equals(960,290)))){
           rightDirections(Heading.UP);
       } else if(((segmentLocations.get(0).point.equals(1220,290)))){
           rightDirections(Heading.RIGHT);
       } else if(((segmentLocations.get(0).point.equals(1220,470)))){
           rightDirections(Heading.UP);
       }

    }


    void beginMoving() {
        rightDirections(heading);
    }


//
//    // Handle changing direction
//    void switchHeading(MotionEvent motionEvent) {
//        // Is the tap on the right hand side?
//        if (motionEvent.getX() >= halfWayPoint) {
//            rightDirections(heading);
//        } else {
//            // Rotate left
//            leftDirections(heading);
//        }
//    }


    void createBitmap(int i, Matrix matrix) {
        mBitMaps[i]  = Bitmap
                .createBitmap(mBitMaps[0] ,
                        0, 0, 60, 60, matrix, true);
    }

    void drawBitmap(int i, Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitMaps[i] ,
                segmentLocations.get(0).point.x,
                segmentLocations.get(0).point.y, paint);
    }

    Point enemyLocation() {
        return segmentLocations.get(0).point;
    }


    boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        // Reaches the Location of The Space Station
        if (segmentLocations.get(0).point.equals(1580,470)) {
            dead = true;
        }

        return dead;
    }


    //Heading Methods
    void rightDirections(Heading input) {

        switch (input) {
            // Rotate right
            case UP:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.UP;
                break;

        }

    }

    void leftDirections(Heading input) {

        // Rotate left
        switch (input) {
            case UP:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.UP;
                break;
        }

    }


}

