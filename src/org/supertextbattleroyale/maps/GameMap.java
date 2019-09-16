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

    private GameLauncher launcher;

    private final List<Player> playersOnMap;

    private final int PATHFIND_DISTANCE_INCREMENT = 10;

    public GameMap(GameLauncher launcher, File directory) throws MapLoadException {
        this.settingsFolder = directory;
        this.launcher = launcher;

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
                if(t.isEmpty()) throw new MapLoadException();

                this.matrixMap[w][h] = t.get();
            }
        }
    }

    private int getTileWidth(int panelWidth, int panelHeight) {
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
        return this.tileTypes.stream().filter(t -> t.getSymbol() == symbol).findAny();
    }

    public Tile[][] getMatrixMap() {
        return this.matrixMap;
    }

    /**
     *
     * @param type the class of a Tile child class
     * @return a list of all tiles that are of the selected type
     */
    public List<Pair<Integer,Integer>> getAllTilesFromType(Class<? extends Tile> type) {
        List<Pair<Integer,Integer>> list = new ArrayList<>();
        for(int i = 0; i < this.getWidthCell(); i++)
            for(int j = 0; j < this.getHeightCell(); j++) {
                if(this.matrixMap[i][j].getClass() == type) list.add(new Pair<>(i,j));
            }
        return list;
    }

    /**
     * A BFS alghorithm on a matrix
     * @param zeroTiles a list of "source nodes"
     * @return a int matrix with the distance from the nearest source node
     */
    public int[][] calculateDistances(List<Pair<Integer,Integer>> zeroTiles) {
        //Initialize all tile with MAX_INT
        int[][] distances = new int[this.getWidthCell()][this.getHeightCell()];
        for(int i = 0; i < this.getWidthCell(); i++)
            for(int j = 0; j < this.getHeightCell(); j++)
                distances[i][j] = Integer.MAX_VALUE;

        ArrayDeque<Pair<Integer,Integer>> visit_queue = new ArrayDeque<>();
        //Add all source nodes to the visit queue and set their distance to the source nodes to 0 (gac)
        for(Pair<Integer,Integer> p : zeroTiles) {
            distances[p.getValue0()][p.getValue1()] = 0;
             visit_queue.offer(p);
        }
        Pair<Integer,Integer> u;
        //Do a BFS search
        while(!visit_queue.isEmpty()) {
            u = visit_queue.poll();
            assert(u != null);
            int i = u.getValue0();
            int j = u.getValue1();
            //Visit all neighbours (also diagonal neighbours)
            //TODO: Add a parameter allowDiagonalMovement
            for(int y = Math.max(0,j-1); y <= Math.min(j+1,this.getHeightCell() - 1); y++)
                for(int x = Math.max(0,i-1); x <= Math.min(i+1,this.getWidthCell() - 1); x++)
                    if(x != i || j != y) {
                        if(distances[x][y] == Integer.MAX_VALUE && this.matrixMap[x][y].isTileWalkable()) {
                            distances[x][y] = distances[i][j] + 1;
                            visit_queue.offer(new Pair<>(x,y));
                        }

                    }
        }
        return distances;
    }

    public void printRoomMatrix() {
        System.out.println();
        System.out.printf("MAP: W %d x H %d\n",this.getWidthCell(),this.getHeightCell());
        for(int h = 0; h < this.getHeightCell(); h++) {
            System.out.println();
            for(int w = 0; w < this.getWidthCell(); w++) {
                System.out.print(this.matrixMap[w][h].getSymbol());
            }
        }
    }

    public void printDistancesMatrix(List<Pair<Integer,Integer>> zeroTiles) {
        int[][] distances = calculateDistances(zeroTiles);
        System.out.println();
        for(int j = 0; j < this.getHeightCell(); j++) {
            System.out.println();
            for(int i = 0; i < this.getWidthCell(); i++)  {
                if(distances[i][j] == Integer.MAX_VALUE)
                    System.out.print("X\t");
                else System.out.printf("%d\t",distances[i][j]);
            }
        }

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
        JPanel gamePanel = this.launcher.getMainFrame().getGamePanel();
        int width = this.getTileWidth(gamePanel.getWidth(), gamePanel.getHeight());
        this.CELL_WIDTH = width;
        this.CELL_HEIGHT = width;

        Pair<Integer, Integer> distances = this.getDistanceFromBorders(gamePanel.getWidth(), gamePanel.getHeight());
        this.X_DIST = distances.getValue0();
        this.Y_DIST = distances.getValue1();
    }

    private int getWidthCell() {
        return this.matrixMap.length;
    }

    private int getHeightCell() {
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

    /**
     * Debug the map drawing rectangles for every tile
     *
     * @param g Graphics
     */
    private void printGrid(Graphics2D g) {
        Color ground = new Color(0, 200, 50, 49);
        Color wall = new Color(33, 33, 33, 150);

        Tile[][] tiles = this.launcher.getGameInstance().getCurrentMap().getMatrixMap();

        JPanel panel = this.launcher.getMainFrame().getGamePanel();

        int w1 = Math.floorDiv(panel.getWidth(), tiles.length);
        int w2 = Math.floorDiv(panel.getHeight(), tiles[0].length);

        int tileWidth = Math.min(w1, w2);

        int xDist = (panel.getWidth() - tileWidth * tiles.length) / 2;
        int yDist = (panel.getHeight() - tileWidth * tiles[0].length) / 2;

        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];

            for (int j = 0; j < row.length; j++) {
                g.setColor(tiles[i][j] instanceof Ground ? ground : wall);
                g.fillRect(xDist + i * tileWidth, yDist + j * tileWidth, tileWidth, tileWidth);
                g.setColor(Color.BLACK);
                g.drawRect(xDist + i * tileWidth, yDist + j * tileWidth, tileWidth, tileWidth);
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
