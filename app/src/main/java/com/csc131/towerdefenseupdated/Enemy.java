package com.csc131.towerdefenseupdated;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;

class Enemy {

    // The location in the grid of all the segments
    private ArrayList<TPoint> segmentLocations;

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

    //Testing
    public int distFromTower;


    // Detecting Collisions
    Rect boundingRect;


    Enemy(Context context, String kind) {

        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

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

        for (int i = 0; i < mBitMaps.length; i++) {
            mBitMaps[i] = BitmapFactory
                    .decodeResource(context.getResources(),
                            rDrawable);
        }

        // Modify the bitmaps to face the head
        // in the correct direction
        mBitMaps[0] = Bitmap
                .createScaledBitmap(mBitMaps[0],
                        60, 60, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        createBitmap(1, matrix);
        matrix.preRotate(-90);
        createBitmap(2, matrix);
        matrix.preRotate(180);
        createBitmap(3, matrix);

        boundingRect = new Rect();


    }

    // Get the enemy ready for a new game
    void reset() {
        // Reset the heading
        heading = Heading.UP;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single alien on the left side of the screen, entering the path
        segmentLocations.add(new TPoint(-100, 470));

        // For detecting collision
        boundingRect = new Rect(segmentLocations.get(0).point.x - 20, segmentLocations.get(0).point.y - 20,
                segmentLocations.get(0).point.x + 60, segmentLocations.get(0).point.y + 60);

    }


    void move() {

        // Move the head in the appropriate heading
        // Get the existing head position
        TPoint p = segmentLocations.get(0);

        hitsTheCorner();

        // Move it appropriately
        switch (heading) {
            case UP:
                p.point.y -= 20;
                break;

            case RIGHT:
                p.point.x += 20;
                break;

            case DOWN:
                p.point.y += 20;
                break;

            case LEFT:
                p.point.x -= 20;
                break;
        }

        updateBoundingRect(p);

    }


    void draw(Canvas canvas, Paint paint) {

        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            switch (heading) {
                case RIGHT:
                    drawBitmap(0, canvas, paint);
                    break;
                case LEFT:
                    drawBitmap(1, canvas, paint);
                    break;
                case UP:
                    drawBitmap(2, canvas, paint);
                    break;
                case DOWN:
                    drawBitmap(3, canvas, paint);
                    break;
            }

        }

        paint.setColor(Color.argb(50, 255, 255, 255));
        canvas.drawRect(boundingRect, paint);
        paint.setColor(Color.argb(255, 255, 255, 255));
    }

    void updateBoundingRect(TPoint p) {
        boundingRect.set(p.point.x - 20, p.point.y - 20, p.point.x + 60, p.point.y + 60);
    }


    //Causes Alien to Follow the Path
    void hitsTheCorner() {

        if ((segmentLocations.get(0).point.equals(360, 470))) {
            rightDirections(Heading.LEFT);
        } else if (((segmentLocations.get(0).point.equals(360, 190)))) {
            rightDirections(Heading.UP);
        } else if (((segmentLocations.get(0).point.equals(780, 190)))) {
            rightDirections(Heading.RIGHT);
        } else if (((segmentLocations.get(0).point.equals(780, 630)))) {
            rightDirections(Heading.DOWN);
        } else if (((segmentLocations.get(0).point.equals(400, 630)))) {
            rightDirections(Heading.RIGHT);
        } else if (((segmentLocations.get(0).point.equals(400, 850)))) {
            rightDirections(Heading.UP);
        } else if (((segmentLocations.get(0).point.equals(1180, 850)))) {
            rightDirections(Heading.LEFT);
        } else if (((segmentLocations.get(0).point.equals(1180, 670)))) {
            rightDirections(Heading.DOWN);
        } else if (((segmentLocations.get(0).point.equals(960, 670)))) {
            rightDirections(Heading.LEFT);
        } else if (((segmentLocations.get(0).point.equals(960, 290)))) {
            rightDirections(Heading.UP);
        } else if (((segmentLocations.get(0).point.equals(1220, 290)))) {
            rightDirections(Heading.RIGHT);
        } else if (((segmentLocations.get(0).point.equals(1220, 470)))) {
            rightDirections(Heading.UP);
        }

    }


    void beginMoving() {
        rightDirections(heading);
    }


    void createBitmap(int i, Matrix matrix) {
        mBitMaps[i] = Bitmap
                .createBitmap(mBitMaps[0],
                        0, 0, 60, 60, matrix, true);
    }

    void drawBitmap(int i, Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitMaps[i],
                segmentLocations.get(0).point.x,
                segmentLocations.get(0).point.y, paint);
    }

    Point enemyLocation() {
        return segmentLocations.get(0).point;
    }

    boolean detectHittingSpaceStation(GameState gameState, SoundEngine audioEngine, ExplosionEffectSystem explosionEffectSystem, Rect spaceStationBoundingRect) {
        boolean dead = false;

        if (spaceStationBoundingRect.contains(boundingRect)) {
            //Death Audio
            audioEngine.playStationCollisionAudio();
            // Emits a particle system effect when the alien reaches the Space Station
            explosionEffectSystem.emitParticles(new PointF(this.enemyLocation().x, this.enemyLocation().y));

            dead = true;
            this.reset();

            gameState.mStationHealth += this.alienDamageAmount;
        }

        return dead;
    }


    boolean detectDeath(SoundEngine audioEngine, ExplosionEffectSystem explosionEffectSystem, Rect laserBoundingRect) {
        // Has the enemy died?
        boolean dead = false;

        if(boundingRect.intersect(laserBoundingRect)) {
            //Death Audio
            audioEngine.playEnemyDeadAudio();
            // Emits a particle system effect when the alien reaches the Space Station
            explosionEffectSystem.emitParticles(new PointF(this.enemyLocation().x, this.enemyLocation().y));

            dead = true;
            this.reset();

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

}

