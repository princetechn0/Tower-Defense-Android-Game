package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
        p.setColor(Color.argb(100,255,255,255));
        for(Rect r : controls){
            c.drawRect(r.left, r.top, r.right, r.bottom, p);
        }

        drawRectImage(context, R.drawable.turret, c, 0);
        drawRectImage(context, R.drawable.machinegun, c, 1);
        drawRectImage(context, R.drawable.raygun, c, 2);

        drawRectText("Start Round", c, controls.get(3));
        drawRectText("Restart", c, controls.get(4));

        // Set the colors back
        p.setColor(Color.argb(255,255,255,255));
    }

    ArrayList<Rect> getControls(){
        return controls;
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



}

