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
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Movement extends Status {

    private Point destination;

    public Movement(Player player, Point destination) {
        super(player);
        this.destination = destination;
    }

    @Override
    public Status doStatusAction() {
        player.acquireInfo();

        List<Player> players = player.getAlivePlayersSeen();

        if (!players.isEmpty()) { //a player has been seen
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) { //TODO: implementare una scelta piu' intelligente per la scelta del giocatore da combattere
//                player.decrementActionsLeft(1);
                return new Combat(player);
            } else { //wants to flee
                List<Point> doors = player.getKnownPlaces().stream()
                        .filter(pair -> pair.getValue0() instanceof Door)
                        .map(Pair::getValue1).collect(Collectors.toList());

                MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                        .filter(player::canSeeTile)
                        .forEach(doors::add);

                player.move(player.getNextLocation(doors)); //TODO:

//                player.decrementActionsLeft(1); bonus movement given from fleeing
                return new Flee(player);
            }
        } else { //no player seen
            Point next = player.getNextLocation(Collections.singletonList(destination));

            if (next.equals(destination)) {
                Tile tile = player.getCurrentMap().getTileAt(next);
                if (tile instanceof Door) {
                    GameMap newMap = ((Door) tile).getNextMap();
                    player.setCurrentMap(newMap);

                    player.decrementActionsLeft(1);
                    player.move(next);

                    return new Recon(player);
                } else if (tile instanceof Chest) {
                    ((Chest) tile).collectItems(player);
                    player.decrementActionsLeft(1);
                } else if(destination.equals(player.getMapCenter())) {
                    player.getKnownPlaces().add(new Pair<>(player.getCurrentMap().getTileAt(player.getMapCenter()), player.getMapCenter()));
                }

                return new Movement(player, player.getNextDestination());
            } else {
                player.move(next);
                return new Movement(player, destination);
            }
        }
    }
}