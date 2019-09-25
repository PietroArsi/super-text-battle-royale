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

        List<Player> players = player.getPlayersSeen();

        if (!players.isEmpty()) { //a player has been seen
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) { //TODO: implementare una scelta piu' intelligente per la scelta del giocatore da combattere
//                player.decrementActionsLeft(1);
                return new Combat(players.get(RandomUtils.randomIntRange(0, players.size() - 1)));
            } else { //wants to flee
                List<Point> doors = player.getKnownPlaces().stream()
                        .filter(pair -> pair.getValue0() instanceof Door)
                        .map(Pair::getValue1).collect(Collectors.toList());

                MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                        .filter(player::canSeeTile)
                        .forEach(doors::add);

                player.move(getNextLocation(doors)); //TODO:

//                player.decrementActionsLeft(1); bonus movement given from fleeing
                System.out.println("HERE");
                return new Flee(player);
            }
        } else { //no player seen
            //TODO: fix
            Point next = getNextLocation(Collections.singletonList(destination));

//            System.out.println(player.getName() + " " + player.getLocation() + " next: " + next + " destination: " + destination);

            if (next.equals(destination)) {
                Tile tile = player.getCurrentMap().getTileAt(next);
                if (tile instanceof Door) {
                    GameMap newMap = ((Door) tile).getNextMap();
                    player.setCurrentMap(newMap);

                    player.decrementActionsLeft(1);
                    return new Recon(player);
                } else if (tile instanceof Chest) {
                    ((Chest) tile).collectItems(player);
                }
            } else {
                player.move(next);
            }

            Optional<Point> bestchest = player.getBestChest();

            if (bestchest.isEmpty()) {
                if (next.equals(getMapCenter()) && player.getCurrentMap().getTileAt(next) instanceof Ground) {
                    return new Movement(player, player.getBestDoor());
                } else {
                    return new Movement(player, getMapCenter());
                }
            } else {
                return new Movement(player, bestchest.get());
            }
        }
    }

    private Point getNextLocation(List<Point> destinations) {
        int[][] m = MapUtils.calculateDistances(
                player.getCurrentMap(),
                Filters.filterNonWalkableAndPlayers(player),
                destinations,
                false);

//        MapUtils.printDistancesMatrix(player.getCurrentMap(), destinations);

        Optional<Point> p =  MapUtils.getNextPathStep(
                player.getCurrentMap(),
                m,
                player.getLocation(),
                false);

//        System.out.println(p.isEmpty());

        return p.orElse(player.getLocation());
    }
    
    private Point getMapCenter() {
        Point center = new Point(player.getCurrentMap().getMatrixWidth()/2, player.getCurrentMap().getMatrixHeight()/2);
        return center;
    }
}