package org.supertextbattleroyale.maps;

import org.supertextbattleroyale.exceptions.MapLoadException;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.Wall;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.utils.FileUtils;

import java.io.File;
import java.util.AbstractMap;
import java.util.List;

public class GameMap {

    private Tile[][] matrixMap;

    private final File settingsFolder;

    public final int CELL_WIDTH, CELL_HEIGHT;
    public final int X_DIST, Y_DIST;

    public GameMap(File directory) throws MapLoadException {
        this.settingsFolder = directory;

        this.setupMap(new File(directory, "map.data"));

        int width = this.getTileWidth(512, 512);
        this.CELL_WIDTH = width;
        this.CELL_HEIGHT = width;

        AbstractMap.SimpleEntry<Integer, Integer> distances = this.getDistanceFromBorders(512, 512);
        this.X_DIST = distances.getKey();
        this.Y_DIST = distances.getValue();
    }

    public GameMap(GameMap in) throws MapLoadException {
        this(in.settingsFolder);
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

}
