package org.supertextbattleroyale.players.statuses;

import org.javatuples.Triplet;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.tiles.Chest;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Movement extends Status {

    private Point destination;

    public Movement(Player player, Point destination) {
        super(player);
        this.destination = destination;
    }

    private StatusAction handleFlee() {
        return () -> {
            player.move(player.getNextLocation(Collections.singletonList(player.getBestDoor())));

            return new Flee(player);
        };
    }

    @Override
    public StatusAction getStatusAction() {
        player.acquireInfo();

        List<Player> players = player.getAlivePlayersSeen();

        if (!players.isEmpty()) { //a player has been seen
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) {
                getAttention();
                return () -> new Combat(player);
            } else { //wants to flee
                getAttention();
                return handleFlee();
            }
        } else { //no player seen
            Point next = player.getNextLocation(Collections.singletonList(destination));

            //CASE DESTINATION REACHED
            if (next.equals(destination)) {
                Tile tile = player.getCurrentMap().getTileAt(next);
                if (tile instanceof Door) {
                    return () -> {
                        GameMap newMap = ((Door) tile).getNextMap(player);
                        player.setCurrentMap(newMap);

                        player.decrementActionsLeft(1);

                        return new Recon(player);
                    };
                }

                //CASE DESTINATION CHEST
                if (tile instanceof Chest) {
                    getAttention();

                    return () -> {
                        ((Chest) tile).collectItems(player);
                        player.decrementActionsLeft(1);
                        return new Movement(player, player.getNextDestination());
                    };
                }

                //CASE DESTINATION MAP CENTER
                if (destination.equals(player.getMapCenter())) {
                    return () -> {
                        player.getKnownPlaces().add(new Triplet<>(player.getCurrentMap(), player.getCurrentMap().getTileAt(player.getMapCenter()), player.getMapCenter()));
                        return new Movement(player, player.getNextDestination());
                    };
                }

                //CASE NOTHING RELEVANT HAPPENED
                return () -> new Movement(player, player.getNextDestination());
            } else {
                return () -> {
                    player.move(next);
                    return new Movement(player, destination);
                };
            }
        }
    }
}