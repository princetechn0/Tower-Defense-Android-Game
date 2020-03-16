//package com.csc131.towerdefenseupdated;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Point;
//
//
//public class Enemy{
//
//    // The location of the apple on the grid
//    // Not in pixels
//    private TPoint location;
//    // An image to represent the enemy
//    private Bitmap mBitmapEnemy;
//    //Type of enemy
//    private String enemyKind;
//
//    //enemy Score
//    private int enemyScore;
//
//    // For tracking movement Heading
//    private enum Heading {
//        UP, RIGHT, DOWN, LEFT
//    }
//
//    // Start by heading to the right
//    private Heading heading = Heading.RIGHT;
//
//    //right-0, left-1, up-2, down-3
//    private Bitmap mBitMaps[];
//
//    // The range of values we can choose from
//    // to spawn an apple
//    private TPoint mSpawnRange;
//
//    private int mSize;
//
//
//    /// Set up the apple in the constructor
//    public Enemy(EnemyBuilder enemyBuilder){
//
//        this.location = enemyBuilder.location;
//        this.enemyKind = enemyBuilder.enemyKind;
//        this.enemyScore = enemyBuilder.enemyScore;
//        this.mBitmapEnemy = enemyBuilder.mBitMapEnemy;
//        this.mBitMaps = enemyBuilder.mBitMaps;
//
//
//    }
//
//    public String getEnemyKind() {
//        return enemyKind;
//    }
//
//    public int getEnemyScore() {
//        return enemyScore;
//    }
//
//
//    // Let SnakeGame know where the apple is
//    // SnakeGame can share this with the snake
//    TPoint getLocation(){
//        return location;
//    }
//
//    void draw(Canvas canvas, Paint paint) {
//            switch (heading) {
//                case RIGHT:
//                    drawBitmap(0 ,canvas, paint);
//                    break;
//                case LEFT:
//                    drawBitmap(1 ,canvas, paint);
//                    break;
//                case UP:
//                    drawBitmap(2 ,canvas, paint);
//                    break;
//                case DOWN:
//                    drawBitmap(3 ,canvas, paint);
//                    break;
//            }
//
//    }
//
//    void drawBitmap(int i, Canvas canvas, Paint paint) {
//        canvas.drawBitmap(mBitMaps[i] ,
//                location.point.x,
//                location.point.y, paint);
//    }
//
//    void spawn() {}
//
//
//    // Get the snake ready for a new game
//    void reset() {
//
//        // Reset the heading
//        heading = Heading.UP;
//
//        // Start with a single alien on the left side of the screen, entering the path
//        location = new TPoint(-1,11);
//
//    }
//
//    void move() {
//        // Move the head in the appropriate heading
//        // Get the existing head position
//        TPoint p = location;
//
//        hitsTheCorner();
//
//        // Move it appropriately
//        switch (heading) {
//            case UP:
//                p.point.y--;
//                break;
//
//            case RIGHT:
//                p.point.x++;
//                break;
//
//            case DOWN:
//                p.point.y++;
//                break;
//
//            case LEFT:
//                p.point.x--;
//                break;
//        }
//
//    }
//
//    //Causes Alien to Follow the Path
//    void hitsTheCorner() {
//
//
//        if ((location.point.x == 380 && location.point.y == 470)) {
//            rightDirections(Heading.LEFT);
//        }
//        if ((location.point.x == 100 && location.point.y == 400)) {
//            rightDirections(Heading.UP);
//        }
////        else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.UP);
////        }
////        else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.RIGHT);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.DOWN);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.RIGHT);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.UP);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.LEFT);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.DOWN);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.LEFT);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.UP);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.RIGHT);
////        } else if((location.point.x == 210 && location.point.y == 470)){
////            rightDirections(Heading.UP);
////        }
//    }
//
//        void rightDirections(Heading input) {
//
//            switch (input) {
//                // Rotate right
//                case UP:
//                    heading = Heading.RIGHT;
//                    break;
//                case RIGHT:
//                    heading = Heading.DOWN;
//                    break;
//                case DOWN:
//                    heading = Heading.LEFT;
//                    break;
//                case LEFT:
//                    heading = Heading.UP;
//                    break;
//
//            }
//
//        }
//
//    boolean detectDeath() {
//        // Has the snake died?
//        boolean dead = false;
//
//        // Reaches the Location of The Space Station
//        if (location.point.x == 36 && location.point.y == 11) {
//            dead = true;
//        }
//
//        return dead;
//    }
//
//
//
//    public static class EnemyBuilder {
//        // The location of the apple on the grid
//        // Not in pixels
//        private TPoint location = new TPoint();
//        // The range of values we can choose from
//        // to spawn an apple
//        private TPoint mSpawnRange;
//
//        private int mSize;
//        // An image to represent the enemy
//        private Bitmap mBitMapEnemy;
//        //Type of Enemy
//        private String enemyKind;
//
//        //Enemy Damage Ability
//        private int enemyScore;
//
//
//        //right-0, left-1, up-2, down-3
//        private Bitmap mBitMaps[] = new Bitmap[4];
//
//
//        public EnemyBuilder(Context context, TPoint sr, int s, String type) {
//            this.mSpawnRange = sr;
//            this.mSize =  s;
//            this.location.point.x = 10;
//            this.location.point.y = 470;
//
//            if(type.equals("alien1")) {
//                //Initializing mBitMap Array
//                for (int i = 0; i < mBitMaps.length; i++) {
//                    mBitMaps[i] = BitmapFactory
//                            .decodeResource(context.getResources(),
//                                    R.drawable.alien1);
//                }
//
//            } else {
//                //Initializing mBitMap Array
//                for (int i = 0; i < mBitMaps.length; i++) {
//                    mBitMaps[i] = BitmapFactory
//                            .decodeResource(context.getResources(),
//                                    R.drawable.alien2);
//                }
//
//            }
//
//            // Modify the bitmaps to face the snake head
//            // in the correct direction
//            mBitMaps[0] = Bitmap
//                    .createScaledBitmap(mBitMaps[0],
//                            60, 60, false);
//
//            // A matrix for scaling
//            Matrix matrix = new Matrix();
//            matrix.preScale(-1, 1);
//            mBitMaps[1] = Bitmap
//                    .createBitmap(mBitMaps[0],
//                            0, 0, s, s, matrix, true);
//            matrix.preRotate(-90);
//            mBitMaps[2] = Bitmap
//                    .createBitmap(mBitMaps[0],
//                            0, 0, s, s, matrix, true);
//            matrix.preRotate(180);
//            mBitMaps[3] = Bitmap
//                    .createBitmap(mBitMaps[0],
//                            0, 0, s, s, matrix, true);
//        }
//
//
//        public EnemyBuilder enemyType(Context context, String enemyKind) {
//
//            if(enemyKind.equals("alien1")) {
//                this.enemyScore = -1;
//                this.mBitMapEnemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien1);
//            }
//
//            if(enemyKind.equals("alien2")) {
//                this.enemyScore = -2;
//                this.mBitMapEnemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien2);
//            }
//
//            this.enemyKind = enemyKind;
//            this.mBitMapEnemy = Bitmap.createScaledBitmap(mBitMapEnemy, mSize, mSize, false);
//            return this;
//
//        }
//
//
//    }
//}
