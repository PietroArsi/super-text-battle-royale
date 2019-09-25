package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.io.File;

public class Door extends Tile {
    public Door(File config) throws JsonLoadFailException {
        super(config);
        //TODO Implement entrance and exit functions
    }

    @Override
    public boolean isTileWalkable() {
        return true;
    }

    @Override
    public boolean isTileBulletproof() {
        return true;
    }

    @Override
    public boolean isTileTransparent() {
        return true;
    }

    //TODO: change
    public GameMap getNextMap() {
        return GameLauncher.getLoadedMaps().get(1);
    }
}
