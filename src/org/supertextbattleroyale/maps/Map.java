package org.supertextbattleroyale.maps;

import org.supertextbattleroyale.exceptions.MapLoadException;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.utils.FileUtils;

import java.io.File;
import java.util.List;

public class Map {

    private Tile[][] matrixMap;

    public Map(File directory) {

    }

    private void setupMap(File config) throws MapLoadException {
        List<String> mapToString = FileUtils.getLinesFromFile(config);

        if(mapToString.size() == 0) throw new MapLoadException();

        int width = mapToString.get(0).length();
        int height = mapToString.size();

        this.matrixMap = new Tile[width][height];

        for (int h = 0; h < mapToString.size(); h++) {
            String line = mapToString.get(h);

            if(line.length() != width) throw new MapLoadException();

            for(int w = 0; w < line.length(); w++) {
                this.matrixMap[w][h] = getTile(line.charAt(w));
            }
        }
    }

    private Tile getTile(char symbol) {
        Tile tile = new Ground();

        return tile;
    }

}
