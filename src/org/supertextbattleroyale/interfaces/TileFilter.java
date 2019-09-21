package org.supertextbattleroyale.interfaces;

import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.awt.*;

@FunctionalInterface
public interface TileFilter {

    /**
     * Interface that allow to choose what blocks could be crossed by an entity
     * @param m The current gameMap
     * @param t The point to check
     * @return true if the entity can cross the tile in (t.x, t.y)
     */
    boolean canCross(GameMap m, Point t);

}
