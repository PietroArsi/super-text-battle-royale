package org.supertextbattleroyale.maps;

import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.exceptions.MapLoadException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.maps.tiles.*;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameMap implements Drawable {

    private static List<Tile> tileTypes;

    private Tile[][] matrixMap;

    private final File settingsFolder;

    public int CELL_WIDTH, CELL_HEIGHT;
    public int X_DIST, Y_DIST;

    private BufferedImage texture;

    private final List<Player> playersOnMap;

    private final int PATHFIND_DISTANCE_INCREMENT = 10;

    public GameMap(File directory) throws MapLoadException {
        this.settingsFolder = directory;

        this.setupMap(new File(directory, "map.data"));
        this.setupTexture(settingsFolder);

        this.playersOnMap = new CopyOnWriteArrayList<>();
    }

    /**
     * Reads all characters from map.data files, gets the corresponding Tile
     * from the read char, and fills the matrixMap with the read tiles
     *
     * @param config
     * @throws MapLoadException
     */
    private void setupMap(File config) throws MapLoadException {
        List<String> mapToString = FileUtils.getLinesFromFile(config);

        if (mapToString.size() == 0) throw new MapLoadException();

        int width = mapToString.get(0).length();
        int height = mapToString.size();

        this.matrixMap = new Tile[width][height];

        for (int h = 0; h < mapToString.size(); h++) {
            String line = mapToString.get(h);

            if (line.length() != width) throw new MapLoadException();

            for (int w = 0; w < line.length(); w++) {
                int value = Integer.parseInt(line.charAt(w) + "");
                Optional<Tile> t = getTile(line.charAt(w));
                if (t.isEmpty()) throw new MapLoadException();

                this.matrixMap[w][h] = t.get();
            }
        }
    }

    private int getTileEdge(int panelWidth, int panelHeight) {
        int w1 = Math.floorDiv(panelWidth, this.matrixMap.length);
        int w2 = Math.floorDiv(panelHeight, this.matrixMap[0].length);

        return Math.min(w1, w2);
    }

    private Pair<Integer, Integer> getDistanceFromBorders(int panelWidth, int panelHeight) {
        int xDist = (panelWidth - CELL_WIDTH * matrixMap.length) / 2;
        int yDist = (panelHeight - CELL_HEIGHT * matrixMap[0].length) / 2;

        return new Pair<>(xDist, yDist);
    }

    //TODO: Find a good method to get from char the corresponding tile
    private Optional<Tile> getTile(char symbol) {
        return tileTypes.stream().filter(t -> t.getSymbol() == symbol).findAny();
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

    /**
     * Setups the current tick Tile dimension based on the Window dimension
     */
    private void setupDimensions() {
        JPanel gamePanel = GameLauncher.getMainFrame().getGamePanel();
        int edgeLength = this.getTileEdge(gamePanel.getWidth()*2/3, gamePanel.getHeight());
        this.CELL_WIDTH = edgeLength;
        this.CELL_HEIGHT = edgeLength;

        Pair<Integer, Integer> distances = this.getDistanceFromBorders(gamePanel.getWidth()*2/3, gamePanel.getHeight());
        this.X_DIST = distances.getValue0();
        this.Y_DIST = distances.getValue1();
    }

    public int getMatrixWidth() {
        return this.matrixMap.length;
    }

    public int getMatrixHeight() {
        return this.matrixMap[0].length;
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

        this.printGrid(g); //debug
    }

    public Point getMapCenter() {
        //TODO: Creare un metodo spiraloso che cerca il centro
        return null;
    }

    /**
     * Debug the map drawing rectangles for every tile
     *
     * @param g Graphics
     */
    private void printGrid(Graphics2D g) {
        Color ground = new Color(0, 200, 50, 49);
        Color wall = new Color(33, 33, 33, 150);

        Tile[][] tiles = GameLauncher.getGameInstance().getCurrentMap().getMatrixMap();

        JPanel panel = GameLauncher.getMainFrame().getGamePanel();

        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];

            for (int j = 0; j < row.length; j++) {
                g.setColor(tiles[i][j] instanceof Ground ? ground : wall);
                g.fillRect(X_DIST + i * CELL_WIDTH, Y_DIST + j * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(X_DIST + i * CELL_WIDTH, Y_DIST + j * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    public List<Player> getPlayersOnMap() {
        return playersOnMap;
    }

    /**
     * Where all types of tiles are created
     *
     * @param config tiles.json
     * @throws JsonLoadFailException if a tile has her symbol stored in the config file
     */
    public static void loadTileTypes(File config) throws JsonLoadFailException {
        tileTypes = new ArrayList<>();
        tileTypes.add(new Chest(config));
        tileTypes.add(new Door(config));
        tileTypes.add(new Ground(config));
        tileTypes.add(new LowObstacle(config));
        tileTypes.add(new Wall(config));
    }
}
