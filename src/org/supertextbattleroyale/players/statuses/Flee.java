package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Flee extends Status {

    public Flee(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        player.acquireInfo();

        Point door = player.getBestDoor();

        Point next = player.getNextLocation(Collections.singletonList(door));
        player.move(next);
        Tile current = player.getCurrentMap().getTileAt(player.getLocation());
        player.decrementActionsLeft(1);

        if (current instanceof Door) {
            player.setCurrentMap(((Door) current).getNextMap());
            return new Recon(player);
        } else {
            return new Flee(player);
        }
    }
}