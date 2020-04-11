package com.csc131.towerdefenseupdated;

import android.content.Context;

import java.util.ArrayList;

public class Level {

    ArrayList<Enemy> enemyArrayList;
    int one, two, three, total;
    Renderer mRenderer;


    Level(Context context, Renderer mRenderer, ArrayList<Enemy> enemyArrayList, int numAlien1, int numAlien2, int numAlien3) {
        this.mRenderer = mRenderer;

        this.enemyArrayList = enemyArrayList;
        one = numAlien1;
        two = numAlien2;
        three = numAlien3;
        total = numAlien1+numAlien2+numAlien3;

        levelCreator(context);
    }

    void levelCreator(Context context) {
        for(int i = 0; i < one; i++) {
            newEnemy(context, "alien1");
        }

        for(int i = 0; i < two; i++) {
            newEnemy(context, "alien2");
        }

        for(int i = 0; i < three; i++) {
            newEnemy(context, "alien3");
        }
    }

    void enemyIncrementer(GameState gameState) {
        // Wave 1
       if(gameState.mRound < 7) {
           gameState.num_enemy1 += 2;
       }
       // Wave 2
       if (gameState.mRound > 7 && gameState.mRound < 13) {
           gameState.num_enemy1 += 1;
           gameState.num_enemy2 += 1;
       }
       // Wave 3
        if (gameState.mRound > 13 && gameState.mRound < 25) {
            gameState.num_enemy1 += 1;
            gameState.num_enemy2 += 1;
            gameState.num_enemy3 += 1;
        }
        if(gameState.mRound > 25) {
            gameState.num_enemy1 += 2;
            gameState.num_enemy2 += 1;
            gameState.num_enemy3 += 1;
        }
    }

    int getTotal() {
        return total;
    }

    public void newEnemy(Context context, String kind) {
        enemyArrayList.add(new Enemy(context, kind));
    }

    public void clear() {
        enemyArrayList.clear();
    }



}
