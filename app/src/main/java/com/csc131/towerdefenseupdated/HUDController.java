package com.csc131.towerdefenseupdated;

import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;

class HUDController implements InputObserver {

    GameEngine gameEngine;

    public HUDController(HUDBroadcaster b, GameEngine ge) {

        b.addObserver(this);
        this.gameEngine = ge;
    }


    @Override
    public void handleInput( MotionEvent event, GameState gameState, ArrayList<Rect> buttons) {
        int i = event.getActionIndex();
        int x = (int) event.getX(i);
        int y = (int) event.getY(i);

        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if(eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP) {

            if (buttons.get(HUD.START_Round).contains(x, y)) {
                   gameState.startRound();
                   gameEngine.nextRound();

            }

            if (buttons.get(HUD.RESTART).contains(x, y)) {
                gameState.newGame();
                gameEngine.newGame();
            }






        }


    }


}
