package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

  class HUDController implements InputObserver {

    GameEngine gameEngine;

    public HUDController(HUDBroadcaster b, GameEngine ge) {
        b.addObserver(this);
        this.gameEngine = ge;
    }



    @Override
    public void handleInput(View v, MotionEvent event, GameState gameState, ArrayList<Rect> buttons, Tower1 tower1) {
        int i = event.getActionIndex();
        int x = (int) event.getX(i);
        int y = (int) event.getY(i);

        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if(eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP) {



            tower1.towerLocation();
            tower1.placeOnMap(event, gameState, x, y );
            tower1.towerLocation();


            // UI
            if (buttons.get(HUD.START_Round).contains(x, y)) {
                    //Disables Start Button while round is running and game is in edit mode
                    if(gameState.mEndofRound && !gameState.mEditing) {
                        gameEngine.nextRound();
                        gameState.startRound();
                    }
            }

            if (buttons.get(HUD.RESTART).contains(x, y)) {
                gameState.newGame();
                gameEngine.newGame();

            }


            // Towers
            if (buttons.get(HUD.TOWER1).contains(x, y)) {
                gameState.mEditing = true;
            }

            if (buttons.get(HUD.TOWER2).contains(x, y)) {
                gameState.mEditing = true;

            }
            if (buttons.get(HUD.TOWER3).contains(x, y)) {
                gameState.mEditing = true;

            }


        }


    }


}
