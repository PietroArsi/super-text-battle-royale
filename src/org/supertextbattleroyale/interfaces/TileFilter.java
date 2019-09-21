package org.supertextbattleroyale.interfaces;

import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.awt.*;

@FunctionalInterface
public interface TileFilter {
    boolean canCross(GameMap m, Point t);
}
