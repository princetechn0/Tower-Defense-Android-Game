package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

class HUD {
    private int mTextFormatting;
    private int mScreenHeight;
    private int mScreenWidth;

    private ArrayList<Rect> controls;
    static int TOWER1 = 0;
    static int TOWER2 = 1;
    static int TOWER3 = 2;
    static int START_Round = 3;
    static int RESTART = 4;

    public ArrayList<Rect> offLimitAreas;

    public ArrayList<Rect> extensiveControls;



    // Game Over
    Rect gameOver = new Rect(1000, 80, 1250, 150);


    HUD(TPoint size) {
        mScreenHeight = size.point.y;
        mScreenWidth = size.point.x;
        mTextFormatting = size.point.x / 35;

        prepareControls();
    }



    private void prepareControls() {
        Rect tower1 = new Rect(1300, 90, 1428, 180);
        Rect tower2 = new Rect(1450, 90, 1578, 180);
        Rect tower3 = new Rect(1600, 90, 1728, 180);

        Rect start_round = new Rect(1550, 1000, 1800, 1090);
        Rect restart = new Rect(0, 1000, 200, 1090);

        controls = new ArrayList<>();
        controls.add(TOWER1, tower1);
        controls.add(TOWER2, tower2);
        controls.add(TOWER3, tower3);
        controls.add(START_Round, start_round);
        controls.add(RESTART, restart);


        // For Warning During Editing
        Rect off1 = new Rect(0, 470, 420, 540);
        Rect off2 = new Rect(340, 175, 420, 540);
        Rect off3 = new Rect(340, 175, 845, 245);
        Rect off4 = new Rect(765, 175, 845, 690);
        Rect off5 = new Rect(395, 615, 845, 690);
        Rect off6 = new Rect(395, 615, 465, 910);
        Rect off7 = new Rect(395, 835, 1245, 910);
        Rect off8 = new Rect(1175, 650, 1245, 910);
        Rect off9 = new Rect(950, 650, 1245, 730);
        Rect off10 = new Rect(950, 270, 1030, 730);
        Rect off11 = new Rect(950, 270, 1275, 345);
        Rect off12 = new Rect(1200, 270, 1275, 535);
        Rect off13 = new Rect(1200, 460, 1430, 535);
        Rect spaceStationBounds = new Rect(1430, 250, 1800, 650);

        offLimitAreas = new ArrayList<>();
        offLimitAreas.add(off1);offLimitAreas.add(off2);
        offLimitAreas.add(off3);offLimitAreas.add(off4);
        offLimitAreas.add(off5);offLimitAreas.add(off6);
        offLimitAreas.add(off7);offLimitAreas.add(off8);
        offLimitAreas.add(off9);offLimitAreas.add(off10);
        offLimitAreas.add(off11);offLimitAreas.add(off12);
        offLimitAreas.add(off13);offLimitAreas.add(spaceStationBounds);


        // For Actions while Tower is Clicked
        Rect towerInfo = new Rect( 500, 0, 1250, 150);
        Rect buy = new Rect( 1000, 80, 1250, 150);
        Rect sell = new Rect(700, 1000, 1100, 1090);

        extensiveControls = new ArrayList<>();
        extensiveControls.add(towerInfo); extensiveControls.add(sell);
        extensiveControls.add(buy);
    }


    void draw(Context context, Canvas canvas, Paint paint, GameState gs) {

        // Draw the HUD
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextSize(mTextFormatting);

        canvas.drawText("Round: " + gs.getRoundNumber(), mTextFormatting, mTextFormatting,paint);
        canvas.drawText("Money: " + gs.getCurrency(), mTextFormatting,mTextFormatting * 2,paint);
        canvas.drawText("Lives: " + gs.getmStationHealth(), mTextFormatting,mTextFormatting * 3,paint);

        canvas.drawText("Towers: ", mTextFormatting + 1375 , mTextFormatting + 10, paint);

        drawControls(context, canvas, paint);

    }



    private void drawControls(Context context, Canvas c, Paint p) {
        p.setColor(Color.argb(100, 255, 255, 255));

        for (Rect r : controls) {
            c.drawRect(r.left, r.top, r.right, r.bottom, p);
        }

        drawRectImage(context, R.drawable.turret, c, 0);
        drawRectImage(context, R.drawable.machinegun, c, 1);
        drawRectImage(context, R.drawable.raygun, c, 2);

        drawRectText("Start Round", c, controls.get(3));
        drawRectText("Restart", c, controls.get(4));

        // Set the colors back
        p.setColor(Color.argb(255, 255, 255, 255));
    }


    private void drawRectImage(Context context, int icon, Canvas c, int i) {
        Drawable drawable = context.getResources().getDrawable(icon);
        drawable.setBounds(controls.get(i));
        drawable.draw(c);
    }

    private void drawRectText(String text, Canvas canvas, Rect r) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(35);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();

        int numOfChars = textPaint.breakText(text,true,width,null);
        int start = (text.length()-numOfChars)/2;
        canvas.drawText(text,start,start+numOfChars,r.exactCenterX(),r.exactCenterY(),textPaint);
    }

    // Warns user of zone where placing Tower is not allowed
    void drawWarningZone(Canvas c , Paint p) {
        p.setColor(Color.argb(150,150,0,0));
        for(Rect r: offLimitAreas){
            c.drawRect(r.left, r.top, r.right, r.bottom, p);
        }
    }

    void drawBuyingControls(Canvas canvas, Paint paint, GameState gs) {
        int towerCost;
        String towerType;

        paint.setColor(Color.argb(255,57,97,173));
        for(Rect r: extensiveControls){
            if(extensiveControls.indexOf(r) != 1){
                if(extensiveControls.indexOf(r) == 2){
                    // Custom Buy Button Color
                    paint.setColor(Color.argb(255, 255, 255, 255));
                }
                canvas.drawRect(r.left, r.top, r.right, r.bottom, paint);

            }
        }

        switch (gs.activeBuyer) {
            case 2:
                towerCost = 850;
                towerType = "Ray Gun";
                break;
            case 1:
                towerCost = 400;
                towerType = "Machine Gun";
                break;
            case 0:
            default:
                towerCost = 250;
                towerType = "Turret";
                break;
        }


        drawRectText("Buy: " + towerCost, canvas, extensiveControls.get(2));

        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(mTextFormatting/2 + 15);
        canvas.drawText("Tower Info", mTextFormatting + 725 , mTextFormatting, paint);

        paint.setTextSize(mTextFormatting/2 + 10);
        canvas.drawText("Type: " + towerType, mTextFormatting + 500 , mTextFormatting + 50, paint);

    }


    void drawExtensiveControls(Canvas canvas, Paint paint, GameState gs, ArrayList<Tower1> tower1ArrayList) {
        paint.setColor(Color.argb(255,57,97,173));

        for(Rect r: extensiveControls){
            if(extensiveControls.indexOf(r) != 2){
                if(extensiveControls.indexOf(r) == 1) {
                    paint.setColor(Color.argb(255,198,105,107));
                }
                canvas.drawRect(r.left, r.top, r.right, r.bottom, paint);

            }
        }

        drawRectText("Sell for: " + tower1ArrayList.get(gs.activeTower).sellPrice, canvas, extensiveControls.get(1));

        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(mTextFormatting/2 + 15);
        canvas.drawText("Tower Info", mTextFormatting + 725 , mTextFormatting, paint);

        paint.setTextSize(mTextFormatting/2 + 10);
        canvas.drawText("Type: " + tower1ArrayList.get(gs.activeTower).name, mTextFormatting + 500 , mTextFormatting + 50, paint);
        canvas.drawText("Speed: " + tower1ArrayList.get(gs.activeTower).speedDesc, mTextFormatting + 950 , mTextFormatting + 50, paint);

    }

//    void gameOver(GameState gs, Canvas canvas, Paint paint) {
//        // Draw the HUD
//        paint.setColor(Color.argb(255, 255, 0, 0));
//        paint.setTextSize(mTextFormatting);
//
//        canvas.drawText("You Survived  " + gs.getRoundNumber() + " Rounds. Better luck next time" , mTextFormatting, mTextFormatting,paint);
//
//        paint.setColor(Color.argb(200,200,0,0));
//        canvas.drawRect(gameOver, paint);
//
//
//    }

    void disableStartButton(Canvas c, Paint p) {
        p.setColor(Color.argb(200,200,0,0));
        c.drawRect(controls.get(3), p);
    }



    ArrayList<Rect> getControls(){
        return controls;
    }

    ArrayList<Rect> getOffLimitAreas(){
        return offLimitAreas;
    }

    ArrayList<Rect> getExtensiveControls(){
        return extensiveControls;
    }




}

