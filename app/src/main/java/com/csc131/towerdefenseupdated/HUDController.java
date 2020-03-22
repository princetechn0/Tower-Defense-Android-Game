package com.csc131.towerdefenseupdated;

import android.content.Context;
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
    public void handleInput(Context context, Renderer mRenderer, MotionEvent event, GameState gameState, ArrayList<Rect> buttons, ArrayList<Tower1> tower1ArrayList) {
        int i = event.getActionIndex();
        int x = (int) event.getX(i);
        int y = (int) event.getY(i);

        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if(eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP) {


            // Places the Latest instantiation of Tower on the Map
            if(tower1ArrayList.size()!=0){
                System.out.println("size" + tower1ArrayList.size());
                tower1ArrayList.get(tower1ArrayList.size()-1).placeOnMap(gameState, x, y);
            }


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
                tower1ArrayList.add(new Tower1(context,
                        new TPoint(mRenderer.NUM_BLOCKS_WIDE,
                                mRenderer.mNumBlocksHigh), "tower1"));
                gameState.mEditing = true;
            }

            if (buttons.get(HUD.TOWER2).contains(x, y)) {
                tower1ArrayList.add(new Tower1(context,
                        new TPoint(mRenderer.NUM_BLOCKS_WIDE,
                                mRenderer.mNumBlocksHigh), "tower2"));
                gameState.mEditing = true;

            }
            if (buttons.get(HUD.TOWER3).contains(x, y)) {
                tower1ArrayList.add(new Tower1(context,
                        new TPoint(mRenderer.NUM_BLOCKS_WIDE,
                                mRenderer.mNumBlocksHigh), "tower3"));
                gameState.mEditing = true;

            }


        }


    }


}
