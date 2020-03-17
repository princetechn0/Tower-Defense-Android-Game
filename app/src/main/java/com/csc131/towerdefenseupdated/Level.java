package com.csc131.towerdefenseupdated;

import android.content.Context;

import java.util.ArrayList;

public class Level {



    ArrayList<Enemy> enemyArrayList;
    int one, two, three, total;
    Renderer mRenderer;


    Level(Renderer mRenderer, ArrayList<Enemy> enemyArrayList, int numAlien1, int numAlien2, int numAlien3) {
        this.mRenderer = mRenderer;

        this.enemyArrayList = enemyArrayList;
        one = numAlien1;
        two = numAlien2;
        three = numAlien3;
        total = numAlien1+numAlien2+numAlien3;
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

    int getTotal() {
        return total;
    }

    public void newEnemy(Context context, String kind) {
        enemyArrayList.add(new Enemy(context,
                new TPoint(mRenderer.NUM_BLOCKS_WIDE,
                        mRenderer.mNumBlocksHigh),
                mRenderer.blockSize, kind));

    }



}
