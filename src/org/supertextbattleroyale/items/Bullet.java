package org.supertextbattleroyale.items;

import org.supertextbattleroyale.interfaces.Drawable;

import java.awt.*;

public class Bullet implements Drawable {

    private final static float BULLET_SPEED = 0.3f;
    private final static float DISTANCE_BEFORE_DYING = 1.0f;

    private float x;
    private float y;

    private float xTarget;
    private float yTarget;

    private boolean miss;

    public Bullet(float x, float y, float xDest, float yDest, boolean miss) {
        this.x = x;
        this.y = y;
        this.xTarget = xDest;
        this.yTarget = yDest;
        this.miss = miss;
    }

    private void moveBullet() {
        float distance = distanceFromTarget();
        //TODO
    }

    private float distanceFromTarget(){
        return (float) Math.sqrt(Math.pow(x - xTarget,2) + Math.pow(y - yTarget,2));
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
