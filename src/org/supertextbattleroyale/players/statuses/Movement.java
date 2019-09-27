package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Chest;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Movement extends Status {

    private Point destination;

    public Movement(Player player, Point destination) {
        super(player);
        this.destination = destination;
    }

    private StatusAction handleFlee() {
        return () -> {
            List<Point> doors = player.getKnownPlaces().stream()
                    .filter(pair -> pair.getValue0() instanceof Door)
                    .map(Pair::getValue1).collect(Collectors.toList());

            MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                    .filter(player::canSeeTile)
                    .forEach(doors::add);

            player.move(player.getNextLocation(doors));

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

                        player.move(next);
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
                        player.getKnownPlaces().add(new Pair<>(player.getCurrentMap().getTileAt(player.getMapCenter()), player.getMapCenter()));
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