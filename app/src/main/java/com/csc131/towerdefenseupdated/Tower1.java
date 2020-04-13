package com.csc131.towerdefenseupdated;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;

import java.lang.reflect.Array;
import java.util.ArrayList;


class Tower1 {

    // The location in the grid of all the segments
    private ArrayList<TPoint> segmentLocations;


    // For tracking movement Heading
    public enum Heading {
        UP, RIGHT, DOWN, LEFT,
        TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT, TOPLEFT,
        TOPTOPRIGHT, BOTTOMBOTTOMRIGHT, BOTTOMBOTTOMLEFT, TOPTOPLEFT
    }

    // Start by heading to the right
    public Heading heading = Heading.RIGHT;

    // right-0, left-1, up-2, down-3, etc
    private Bitmap mBitMaps[] = new Bitmap[12];

    // Tower Attributes
    public String name;
    public String speedDesc;
    public int cost;
    public int sellPrice;

    public int coolDownRate;

    // Bounding Region when Tower Clicked
    Rect touchRect;
    // Radius for Drawing Circle
    int radius;

    // For Changing Direction of Tower Relative to Enemy Position
    Polygon[] polygons = new Polygon[8];
    Point[] points;


    //Testing Lasers
    TowerLaser towerLaser;

    ArrayList<Enemy> enemiesInACircle = new ArrayList<>();
    ArrayList<Integer> distFromTower = new ArrayList<>();
    int enemyToFollow = 0;
    Boolean enemyInCircle;



    Tower1(Context context, String kind) {

        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();


        int rDrawable;
        switch (kind) {
            case "tower2":
                rDrawable = R.drawable.machinegun;
                coolDownRate = 2;
                cost = 400;
                radius = 400;
                name = "Machine Gun";
                speedDesc = "Medium";
                sellPrice = 320;
                break;
            case "tower3":
                rDrawable = R.drawable.raygun;
                coolDownRate = 3;
                cost = 850;
                radius = 500;
                name = "Ray Gun";
                speedDesc = "Slow";
                sellPrice = 700;
                break;
            case "tower1":
            default:
                rDrawable = R.drawable.turret;
                coolDownRate = 1;
                cost = 250;
                radius = 250;
                name = "Turret";
                speedDesc = "Fast";
                sellPrice = 200;
                break;
        }


        for(int i = 0; i < mBitMaps.length; i++){
            mBitMaps[i] = BitmapFactory
                    .decodeResource(context.getResources(),rDrawable);
        }

        // Modify the bitmaps to face the tower
        // in the correct direction
        mBitMaps[0]  = Bitmap
                .createScaledBitmap(mBitMaps[0] ,
                        150, 150, false);

        // A matrix for scaling
        matrixScaling();

        // Start by placing Tower off screen before user calls it
        segmentLocations.add(new TPoint(-500, -500));

        // Initialize Boundary for detecting touch on specific tower
        touchRect = new Rect();

        towerLaser = new TowerLaser(context, kind);


    }

    // Get the Tower ready for a new game
    void reset(int x, int y) {
        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single tower
        segmentLocations.add(new TPoint(x,y));

        touchRect = new Rect(x-40,y-40,x+80, y+80);

        // Testing lasers
        resetLaserToTowerPos();
    }


    //Testing diff function
    double valX;
    double valY;
    void update(Point enemyLocation, float delta) {
        double theta = Math.atan2(enemyLocation.y - towerLocation().y, enemyLocation.x - towerLocation().x);
         valX = (delta * 2) * Math.cos(theta);
         valY = (delta * 2) * Math.sin(theta);
    }

    // Reset laser to tower location ready for next shot
    void resetLaserToTowerPos() {
        towerLaser.reset(segmentLocations.get(0).point.x, segmentLocations.get(0).point.y);
    }


    Boolean hasFired = false;
    void updateLaser(Point enemyLocation) {

        // if hasnt been shot yet
        if(!hasFired) {
            resetLaserToTowerPos();
            // get enemy location
            update(enemyLocation, 20);

            //fire
            hasFired = true;
        }
        shootLaser();

        if(!pointInCircle(towerLaser.laserLocation(), towerLocation(), radius)) {
            hasFired = false;

            hideLasers();

        }



//        // if laser in circle
//        if(pointInCircle(towerLaser.laserLocation(), towerLocation(), radius)) {
//
//            // if hasnt been shot yet
//            if(!hasFired) {
//                // get enemy location
//                update(enemyLocation, 30);
//
//                //fire
//                hasFired = true;
//            }
//
//            //fire
//            shootLaser();
//
//        } else {
//
//            resetLaserToTowerPos();
//            hasFired = false;
//        }

    }

    void hideLasers() {
        towerLaser.resetLaserOffscreen();
    }

    void shootLaser() {
        towerLaser.move(valX, valY);
    }


    void drawRadius(Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(50,255,255,255));

        RectF oval = new RectF(segmentLocations.get(0).point.x - radius, segmentLocations.get(0).point.y - radius,
                segmentLocations.get(0).point.x + radius + 60, segmentLocations.get(0).point.y + radius + 60);
        canvas.drawOval(oval, paint);
        getPoints((int)oval.centerX(), (int)oval.centerY(), radius + 25, 8);

        initDirections();
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
                case TOPRIGHT:
                    drawBitmap(4 ,canvas, paint);
                    break;
                case BOTTOMRIGHT:
                    drawBitmap(5 ,canvas, paint);
                    break;
                case BOTTOMLEFT:
                    drawBitmap(6 ,canvas, paint);
                    break;
                case TOPLEFT:
                    drawBitmap(7 ,canvas, paint);
                    break;
                case TOPTOPRIGHT:
                    drawBitmap(8 ,canvas, paint);
                    break;
                case BOTTOMBOTTOMRIGHT:
                    drawBitmap(9 ,canvas, paint);
                    break;
                case BOTTOMBOTTOMLEFT:
                    drawBitmap(10 ,canvas, paint);
                    break;
                case TOPTOPLEFT:
                    drawBitmap(11 ,canvas, paint);
                    break;
            }

        }
    }


    // Handles Placing Tower on Map
    void placeOnMap(GameState gameState, int x, int y, int cost) {
        if(gameState.mEditing == true) {

            reset(x,y);

            // Disables Editing Mode after Tower is moved Initially
            gameState.mEditing = false;

            // Subtracts Cost
            gameState.mCurrency -= cost;

        }

    }

    void resetDirection() {
        heading = heading.RIGHT;
    }

    // Gets position of enemy or laser relative to Tower
    boolean pointInCircle(Point enemyLocation, Point towerLocation, int radius) {
        int distancesquared = distFromTower(enemyLocation, towerLocation);
        return distancesquared <= radius * radius;
    }
    int distFromTower(Point enemyLocation, Point towerLocation) {
        return (enemyLocation.x - towerLocation.x) * (enemyLocation.x - towerLocation.x) +
                (enemyLocation.y - towerLocation.y) * (enemyLocation.y - towerLocation.y);
    }


    void rotateTower(Point enemyPosition) {
        for(int i = 0; i < 8; i++ ){
            if(polygons[i].contains(enemyPosition.x, enemyPosition.y, i)) {
                rightDirections(polygons[i].heading);
            }
        }
    }


    void initDirections() {
        int[][] ptsX = new int[8][3];
        int[][] ptsY = new int[8][3];

        // For 0 - 6
        for(int i = 0; i < points.length-1; i++ ){
            ptsX[i][0] = towerLocation().x; ptsY[i][0] = towerLocation().y;
            ptsX[i][1] = points[i].x; ptsX[i][2] = points[i + 1].x;
            ptsY[i][1] = points[i].y; ptsY[i][2] = points[i + 1].y;

            Heading h;
            switch (i) {
                case 0:
                    h = Heading.BOTTOMRIGHT;
                    break;
                case 1:
                    h = Heading.BOTTOMBOTTOMRIGHT;
                    break;
                case 2:
                    h = Heading.BOTTOMBOTTOMLEFT;
                    break;
                case 3:
                    h = Heading.BOTTOMLEFT;
                    break;
                case 4:
                    h = Heading.TOPLEFT;
                    break;
                case 5:
                    h = Heading.TOPTOPLEFT;
                    break;
                case 6:
                    h = Heading.TOPTOPRIGHT;
                    break;
                default:
                    h = Heading.RIGHT;
                    break;
            }
            polygons[i] = new Polygon(ptsX, ptsY,3, h);
        }

        // Manual Assignment of 7 Due to Different Behavior
        ptsX[7][0] = towerLocation().x; ptsY[7][0] = towerLocation().y;
        ptsX[7][1] = points[7].x; ptsX[7][2] = points[0].x;
        ptsY[7][1] = points[7].y; ptsY[7][2] = points[0].y;
        polygons[7] = new Polygon(ptsX, ptsY,3, Heading.TOPRIGHT);
    }

    private void getPoints(int x0,int y0,int r,int noOfDividingPoints) {
        double angle;
        points = new Point[noOfDividingPoints];

        for(int i = 0 ; i < noOfDividingPoints  ;i++) {
            angle = i * (360/noOfDividingPoints);
            points[i] = new Point((int) (x0 + r * Math.cos(Math.toRadians(angle))), (int) (y0 + r * Math.sin(Math.toRadians(angle))));
        }
    }

    void createBitmap(int i, Matrix matrix) {
        mBitMaps[i]  = Bitmap
                .createBitmap(mBitMaps[0] ,
                        0, 0, 130, 130, matrix, true);
    }

    void drawBitmap(int i, Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitMaps[i] ,
                segmentLocations.get(0).point.x - 50,
                segmentLocations.get(0).point.y - 50, paint);
    }


    Point towerLocation() {
        return segmentLocations.get(0).point;
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

            case TOPRIGHT:
                heading = heading.TOPRIGHT;
                break;
            case BOTTOMRIGHT:
                heading = heading.BOTTOMRIGHT;
                break;
            case BOTTOMLEFT:
                heading = heading.BOTTOMLEFT;
                break;
            case TOPLEFT:
                heading = heading.TOPLEFT;
                break;

            case TOPTOPRIGHT:
                heading = heading.TOPTOPRIGHT;
                break;
            case BOTTOMBOTTOMRIGHT:
                heading = heading.BOTTOMBOTTOMRIGHT;
                break;
            case BOTTOMBOTTOMLEFT:
                heading = heading.BOTTOMBOTTOMLEFT;
                break;
            case TOPTOPLEFT:
                heading = heading.TOPTOPLEFT;
                break;
        }

    }

    void matrixScaling() {
        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        createBitmap(1, matrix);
        matrix.preRotate(-90);
        createBitmap(2, matrix);
        matrix.preRotate(180);
        createBitmap(3, matrix);
        matrix.preRotate(-45);

        // Diagonal Scaling
        createBitmap(4, matrix);
        matrix.preRotate(-90);
        createBitmap(5, matrix);
        matrix.preScale(-1, 1);
        matrix.preRotate(-90);
        createBitmap(6, matrix);
        matrix.preRotate(90);
        createBitmap(7, matrix);

        // Further Diagonal Scaling
        matrix.preRotate(15);
        createBitmap(11, matrix);
        matrix.preRotate(-115);
        createBitmap(10, matrix);
        matrix.preScale(-1, 1);
        matrix.preRotate(5);
        createBitmap(8, matrix);
        matrix.preRotate(-115);
        createBitmap(9, matrix);
    }

}


