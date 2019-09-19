package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.io.File;

public class Wall extends Tile {

    public Wall(File config) throws JsonLoadFailException {
        super(config);
    }

    @Override
    public boolean isTileWalkable() {
        return false;
    }

    @Override
    public boolean isTileBulletproof() {
        return true;
    }

    @Override
    public boolean isTileTransparent() {
        return false;
    }

}
