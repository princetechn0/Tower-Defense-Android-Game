package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;

interface InputObserver {
    void handleInput( MotionEvent event, GameState gs, ArrayList<Rect> controls);
}
