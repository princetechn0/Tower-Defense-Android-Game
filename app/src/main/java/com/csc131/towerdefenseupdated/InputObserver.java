package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

interface InputObserver {
    void handleInput(Context context, Renderer mRenderer, MotionEvent event, GameState gs, ArrayList<Rect> offLimitAreas,
                     ArrayList<Rect> controls, ArrayList<Rect> extensiveControls, ArrayList<Tower1> tower1ArrayList);
}
