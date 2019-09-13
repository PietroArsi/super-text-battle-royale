package org.supertextbattleroyale.maps;

import org.supertextbattleroyale.Main;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.exceptions.MapLoadException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.game.GameWindow;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.Wall;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.utils.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

public class GameMap implements Drawable {

    private Tile[][] matrixMap;

    private final File settingsFolder;

    public int CELL_WIDTH, CELL_HEIGHT;
    public int X_DIST, Y_DIST;

    private BufferedImage texture;

    private GameLauncher launcher;

    public GameMap(GameLauncher launcher, File directory) throws MapLoadException {
        this.settingsFolder = directory;
        this.launcher = launcher;

        this.setupMap(new File(directory, "map.data"));
        this.setupTexture(settingsFolder);
    }

    public GameMap(GameMap in) throws MapLoadException {
        this(in.launcher, in.settingsFolder);
    }

    private void setupMap(File config) throws MapLoadException {
        List<String> mapToString = FileUtils.getLinesFromFile(config);

        if(mapToString.size() == 0) throw new MapLoadException();

        int width = mapToString.get(0).length();
        int height = mapToString.size();

        this.matrixMap = new Tile[width][height];

        for (int h = 0; h < mapToString.size(); h++) {
            String line = mapToString.get(h);

            if (line.length() != width) throw new MapLoadException();

            for (int w = 0; w < line.length(); w++) {
                int value = Integer.parseInt(line.charAt(w) + "");
                this.matrixMap[w][h] = getTile(line.charAt(w));
            }
        }
    }

    private int getTileWidth(int panelWidth, int panelHeight) {
        int w1 = Math.floorDiv(panelWidth, this.matrixMap.length);
        int w2 = Math.floorDiv(panelHeight, this.matrixMap[0].length);

        return Math.min(w1, w2);
    }

    private AbstractMap.SimpleEntry<Integer, Integer> getDistanceFromBorders(int panelWidth, int panelHeight) {
        int xDist = (panelWidth - CELL_WIDTH * matrixMap.length) / 2;
        int yDist = (panelHeight - CELL_HEIGHT * matrixMap[0].length) / 2;

        return new AbstractMap.SimpleEntry<>(xDist, yDist);
    }

    private Tile getTile(char symbol) {
        if(symbol == '0') return new Ground();
        else return new Wall();
    }

    public Tile[][] getMatrixMap() {
        return this.matrixMap;
    }

    private void setupTexture(File settingsFolder) throws MapLoadException {
        try {
            this.texture = ImageIO.read(new FileInputStream(new File(settingsFolder, "map.image")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new MapLoadException();
        }
    }

    private void setupDimensions() {
        JPanel gamePanel = this.launcher.mainFrame.getGamePanel();
        int width = this.getTileWidth(gamePanel.getWidth(), gamePanel.getHeight());
        this.CELL_WIDTH = width;
        this.CELL_HEIGHT = width;

        AbstractMap.SimpleEntry<Integer, Integer> distances = this.getDistanceFromBorders(gamePanel.getWidth(), gamePanel.getHeight());
        this.X_DIST = distances.getKey();
        this.Y_DIST = distances.getValue();
    }

    @Override
    public void draw(Graphics2D g) {
        this.setupDimensions();
        g.drawImage(this.texture,
                this.X_DIST,
                this.Y_DIST,
                this.CELL_WIDTH * this.matrixMap.length,
                this.CELL_HEIGHT * this.matrixMap[0].length,
                null);
    }
}