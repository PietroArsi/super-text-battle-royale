package org.supertextbattleroyale.maps.tiles;


import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.io.File;

public class LowObstacle extends Tile
{
    public LowObstacle(File config) throws JsonLoadFailException {
        super(config);
    }

    @Override
    public boolean isTileWalkable() {
        return false;
    }
    @Override
    public boolean isTileBulletproof() {
        return false;
    }

    @Override
    public boolean isTileTransparent() {
        return true;
    }
}
