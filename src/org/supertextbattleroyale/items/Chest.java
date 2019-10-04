package org.supertextbattleroyale.items;

import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chest implements Drawable {
    private boolean empty;
    private List<Collectible> items;
    private List<BufferedImage> images;
    private boolean open, animate;

    private int x, y;

    public Chest(int x, int y) {
        this.x = x;
        this.y = y;

        this.items = new ArrayList<>();
        this.images = new ArrayList<>();
        this.empty = false;
    }

    public Chest(Point p) {
        this(p.x, p.y);
    }

    public void addItems(Collectible... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public List<Collectible> getItems() {
        return this.items;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public List<Collectible> loot() {
        empty = true;
        return this.items;
    }

    public void collectItems(Player player) {
    }

    public void open() {
        this.open = true;
        this.animate = true;
    }

    public void close() {
        this.open = false;
        this.animate = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.open ? Color.red : Color.yellow);
        GameMap map = GameLauncher.getGameInstance().getCurrentMap();

        g.translate(map.X_DIST + map.CELL_WIDTH * x, map.Y_DIST + map.CELL_HEIGHT * y);

        g.fillRect((int) (map.CELL_WIDTH * 0.1f),
                (int) (map.CELL_HEIGHT * 0.1f),
                (int) (map.CELL_WIDTH * 0.8f),
                (int) (map.CELL_HEIGHT * 0.8f));

        g.translate(-map.X_DIST - map.CELL_WIDTH * x, -map.Y_DIST - map.CELL_HEIGHT * y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getLocation() {
        return new Point(x, y);
    }
}
