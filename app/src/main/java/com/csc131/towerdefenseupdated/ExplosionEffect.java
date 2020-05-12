package com.csc131.towerdefenseupdated;

import android.graphics.PointF;


class ExplosionEffect {
    PointF mVelocity;
    PointF mPosition;

    ExplosionEffect(PointF direction) {
        mVelocity = new PointF();
        mPosition = new PointF();
        // Determine the direction
        mVelocity.x = direction.x;
        mVelocity.y = direction.y;

    }

    void update()
    {
        // Move the particle
        mPosition.x += mVelocity.x; mPosition.y += mVelocity.y;
    }

    void setPosition(PointF position)
    {
        mPosition.x = position.x;
        mPosition.y = position.y;
    }

    PointF getPosition()
    {
        return mPosition;
    }

}
 class PhysicsEngine {
    boolean update(long fps, ExplosionEffectSystem ps){
        if(ps.mIsRunning){
            ps.update(fps);
        }
        return false;
    }
}
