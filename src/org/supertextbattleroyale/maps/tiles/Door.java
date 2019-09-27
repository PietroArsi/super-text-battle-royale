package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;

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

    public GameMap getNextMap(Player p) {
        GameMap map = GameLauncher.getLoadedMaps().stream().filter(g -> g != p.getCurrentMap()).findAny().orElse(p.getCurrentMap());
        p.move(MapUtils.getAllTilesFromType(map, Door.class).stream().findAny().get());
        return map;
    }
}
