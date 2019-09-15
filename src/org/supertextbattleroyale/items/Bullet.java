package org.supertextbattleroyale.items;

import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet implements Drawable {

    private final static float BULLET_SPEED = 0.3f;
    private final static float DISTANCE_BEFORE_MISS = 1.0f;
    private final static float DISTANCE_BEFORE_HIT = 0.2f;

    private GameMap map;

    private float x;
    private float y;

    private float xTarget;
    private float yTarget;

    private double direction;

    private boolean miss;
    private boolean alive;

    public Bullet(GameMap map, float x, float y, float xDest, float yDest, boolean miss) {
        this.map = map;
        this.xTarget = xDest;
        this.yTarget = yDest;
        this.miss = miss;
        this.alive = true;
        this.direction = Math.atan2(yDest - y, xDest - x);
    }

    private void moveBullet() {
        float distance = distanceFromTarget();
        alive = (distance > DISTANCE_BEFORE_HIT && !miss) || (distance > DISTANCE_BEFORE_MISS && miss);
        if(alive) {
            this.x = this.x +  BULLET_SPEED*(float) Math.cos(this.direction);
            this.y = this.y - BULLET_SPEED*(float) Math.sin(this.direction);
        }
        //TODO
    }


    private float distanceFromTarget(){
        return (float) Math.sqrt(Math.pow(x - xTarget,2) + Math.pow(y - yTarget,2));
    }

    @Override
    public void draw(Graphics2D g) {


    }
}
