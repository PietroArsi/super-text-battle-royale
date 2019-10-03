package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chest extends LowObstacle implements Drawable {
    private boolean empty;
    private List<Collectible> items;
    private List<BufferedImage> images;
    private boolean open, animate;

    //TODO: change tile system since modifying this makes all chest open

    public Chest(File config) throws JsonLoadFailException {
        super(config);

        this.items = new ArrayList<>();
        this.images = new ArrayList<>();
        this.empty = false;

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

        g.fillRect((int) (map.CELL_WIDTH * 0.1f),
                (int) (map.CELL_HEIGHT * 0.1f),
                (int) (map.CELL_WIDTH* 0.8f),
                (int) (map.CELL_HEIGHT * 0.8f));
    }
}
