package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.Chest;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChestTile extends LowObstacle {

    //TODO: change tile system since modifying this makes all chest open

    public ChestTile(File config) throws JsonLoadFailException {
        super(config);
    }

    public Optional<Chest> getChest(GameMap map, Point p) {
        return map.getChestsOnMap().stream()
                .filter(chest -> chest.getLocation().equals(p))
                .findAny();
    }

}
