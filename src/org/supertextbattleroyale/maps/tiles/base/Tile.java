package org.supertextbattleroyale.maps.tiles.base;

public abstract class Tile {

    private boolean isWalkable;
    private boolean isBulletProof; //True if bullet can't pass through tile

    public abstract boolean isTileWalkable();
    public abstract boolean isTileBulletproof();

}
