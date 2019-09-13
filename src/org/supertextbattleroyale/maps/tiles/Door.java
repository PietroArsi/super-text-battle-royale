package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.maps.tiles.base.Tile;

public class Door extends Tile {
    public Door() {
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
}
