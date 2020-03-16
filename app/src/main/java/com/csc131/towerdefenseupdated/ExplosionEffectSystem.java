package com.csc131.towerdefenseupdated;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Random;

public class ExplosionEffectSystem {
    float mDuration;

    ArrayList<ExplosionEffect> mParticles;
    Random random = new Random();
    boolean mIsRunning = false;

    void init(int numParticles){
        mParticles = new ArrayList<>();
        // Create the particles
        for (int i = 0; i < numParticles; i++) {
            float angle = (random.nextInt(360)) * (3.14f / 180.f);
            float speed = (random.nextInt(20)+1);
            PointF direction = new PointF((float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed);
            mParticles.add(new ExplosionEffect(direction));
        }
    }

    void update(long fps){
        mDuration -= (1f/fps);
        for(ExplosionEffect p : mParticles) {
            p.update();
        }

        if (mDuration < 0) {
            mIsRunning = false;
        }

    }

    void emitParticles(PointF startPosition) {
        mIsRunning = true;
        mDuration = 1f;

        for(ExplosionEffect p : mParticles) {
            p.setPosition(startPosition);
        }
    }

    void draw(Canvas canvas, Paint paint) {
        for (ExplosionEffect p : mParticles) {
            paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            // Uncomment the next line to have plain white particles
//            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRect(p.getPosition().x,
                    p.getPosition().y, p.getPosition().x+25 , p.getPosition().y+25 , paint);
        }
    }

}
