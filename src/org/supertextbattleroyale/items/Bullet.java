package org.supertextbattleroyale.items;

import org.supertextbattleroyale.interfaces.Drawable;

import java.awt.*;

public class Bullet implements Drawable {

    private float x;
    private float y;

    private float xDest;
    private float yDest;

    private boolean miss;

    public Bullet(float x, float y, float xDest, float yDest, boolean miss) {
        this.x = x;
        this.y = y;
        this.xDest = xDest;
        this.yDest = yDest;
        this.miss = miss;
    }


    @Override
    public void draw(Graphics2D g) {

    }
}
