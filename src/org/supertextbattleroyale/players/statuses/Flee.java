package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;

import java.awt.*;
import java.util.Collections;

public class Flee extends Status {

    public Flee(Player player) {
        super(player);
    }

    @Override
    public StatusAction getStatusAction() {
        player.acquireInfo();

        Point door = player.getBestDoor();

        Point next = player.getNextLocation(Collections.singletonList(door));
        player.move(next);
        Tile current = player.getCurrentMap().getTileAt(player.getLocation());
        player.decrementActionsLeft(1);

        if (current instanceof Door) {
            player.setCurrentMap(((Door) current).getNextMap(player));
            return () -> new Recon(player);
        } else {
            return () -> new Flee(player);
        }
    }
}