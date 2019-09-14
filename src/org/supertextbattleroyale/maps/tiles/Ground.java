package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.io.File;

public class Ground extends Tile {

    public Ground(File config) throws JsonLoadFailException {
        super(config);
    }

    @Override
    public boolean isTileWalkable() {
        return true;
    }

    @Override
    public boolean isTileBulletproof() {
        return false;
    }

}
