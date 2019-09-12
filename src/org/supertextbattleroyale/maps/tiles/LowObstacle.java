package org.supertextbattleroyale.maps.tiles;


import org.supertextbattleroyale.maps.tiles.base.Tile;

public class LowObstacle extends Tile
{
    @Override
    public boolean isTileWalkable() {
        return false;
    }
    @Override
    public boolean isTileBulletproof() {
        return false;
    }
}
