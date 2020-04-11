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
    public void handleInput(Context context, Renderer mRenderer, MotionEvent event, GameState gameState, ArrayList<Rect> offLimitAreas,
                            ArrayList<Rect> buttons, ArrayList<Rect> extensiveControls, ArrayList<Tower1> tower1ArrayList, Toast toast) {
        int i = event.getActionIndex();
        int x = (int) event.getX(i);
        int y = (int) event.getY(i);

        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if(eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP) {

            // UI
            if (buttons.get(HUD.START_Round).contains(x, y)) {
                    //Disables Start Button while round is running and game is in edit mode
                    if(gameState.mEndofRound && !gameState.mEditing) {
                        gameState.startRound();
                        gameEngine.nextRound();
                    }
            }

            if (buttons.get(HUD.RESTART).contains(x, y)) {
                gameState.newGame();
                gameEngine.newGame();
            }


            // Tower Handler
            if(tower1ArrayList.size() != 0) {
                // Places the Latest instantiation of Tower on the Map
                Boolean offLimit = false;

                for (Rect r : offLimitAreas) {
                    if (r.contains(x, y)) {
                        offLimit = true;
                    }
                }
                if (!offLimit) {
                    tower1ArrayList.get(tower1ArrayList.size() - 1).placeOnMap(gameState, x, y,
                            tower1ArrayList.get(tower1ArrayList.size() - 1).cost);
                }


                // Touching a Rect for Further Editing / Deleting
                gameState.mTowerClicked = false;
                for (Tower1 t : tower1ArrayList) {
                    if (t.touchRect.contains(x, y)) {
                        gameState.mTowerClicked = true;
                        gameState.activeTower = tower1ArrayList.indexOf(t);
                    }
                }


                // Sell Button for Selling the Tower
                if(extensiveControls.get(1).contains(x, y)){
                    gameState.mCurrency += tower1ArrayList.get(gameState.activeTower).sellPrice;
                    tower1ArrayList.remove(gameState.activeTower);
                }
            }


            // Resets mBuying Variable
            gameState.mBuying = false;

            if (buttons.get(HUD.TOWER1).contains(x, y)) {
                gameState.mBuying = true;
                gameState.activeBuyer = HUD.TOWER1;
            }

            if (buttons.get(HUD.TOWER2).contains(x, y)) {
                gameState.mBuying = true;
                gameState.activeBuyer = HUD.TOWER2;
            }

            if (buttons.get(HUD.TOWER3).contains(x, y)) {
                gameState.mBuying = true;
                gameState.activeBuyer = HUD.TOWER3;
            }


            // If Buy Button is Clicked
            if(extensiveControls.get(2).contains(x, y)){
                gameState.mBuying = false;
                // Checks if user can afford the tower
                switch (gameState.activeBuyer) {
                    case 0:
                        towerCreator(gameState, tower1ArrayList, context, mRenderer, 250, "tower1", toast);
                        break;
                    case 1:
                        towerCreator(gameState, tower1ArrayList, context, mRenderer, 400, "tower2", toast);
                        break;
                    case 2:
                        towerCreator(gameState, tower1ArrayList, context, mRenderer, 850, "tower3", toast);
                        break;
                }
            }
        }
    }


      void towerCreator(GameState gameState, ArrayList<Tower1> tower1ArrayList, Context context, Renderer mRenderer,
                        int currency, String towerType, Toast toast) {
          if(gameState.mCurrency >= currency){
              tower1ArrayList.add(new Tower1(context, towerType));
              gameState.mEditing = true;
              toast.onScreenMessages("Good Choice! Now Place it on the Map!");
          } else {
              toast.onScreenMessages("Not Enough Money :(");

          }
      }




}
