package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.util.stream.Collectors;

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
        List<GameMap> m = GameLauncher.getLoadedMaps().stream().filter(g -> g != p.getCurrentMap()).collect(Collectors.toList());
        GameMap map = m.get(RandomUtils.randomIntRange(0, m.size() - 1));
        List<Point> l = MapUtils.getAllTilesFromType(map, Door.class);
        p.move(l.get(RandomUtils.randomIntRange(0, l.size() -1)));
        return map;
    }
}
