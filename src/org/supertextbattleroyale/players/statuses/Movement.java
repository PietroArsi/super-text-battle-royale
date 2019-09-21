package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Chest;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        List<Player> players = player.getPlayersSeen();

        if (!players.isEmpty()) { //a player has been seen
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) { //TODO: implementare una scelta piu' intelligente per la scelta del giocatore da combattere
                return new Combat(players.get(RandomUtils.randomIntRange(0, players.size() - 1)));
            } else { //wants to flee
                List<Point> doors = player.getKnownPlaces().stream()
                        .filter(pair -> pair.getValue0() instanceof Door)
                        .map(Pair::getValue1).collect(Collectors.toList());

                MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                        .filter(player::canSeeTile)
                        .forEach(doors::add);

                player.move(getNextPoint(doors));

                return new Flee(player, null); //TODO: get the door from calculate distances
            }
        } else { //no player seen
            Point next = getNextPoint(Collections.singletonList(destination));

            if (next.equals(destination)) {
                Tile tile = player.getCurrentMap().getTileAt(next);
                if (tile instanceof Door) {
                    GameMap newMap = ((Door) tile).getNextMap();
                    player.setCurrentMap(newMap);

                    return new Recon(player);
                } else if (tile instanceof Chest) {
                    ((Chest) tile).collectItems(player);
                }
            } else {
                player.move(next);
            }

            List<Point> objectives = MapUtils.getAllTilesFromType(player.getCurrentMap(), Chest.class);
            objectives.addAll(MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class));

            if(objectives.size() == 0) {
                return new Movement(player, player.getCurrentMap().getMapCenter());
            } else {
                return null;
                //TODO: return movement al posto piu' vicino
            }
        }
    }

    private Point getNextPoint(List<Point> destinations) {
        int[][] matrix = MapUtils.calculateDistances(
                player.getCurrentMap(),
                (map, p) -> map.getMatrixMap()[p.x][p.y].isTileWalkable(),
                destinations,
                false);

        List<Pair<Point, Integer>> toCheck = new ArrayList<>();
        if (player.getX() > 0)
            toCheck.add(new Pair<>(player.getLocationOffset(-1, 0), matrix[player.getX() - 1][player.getY()]));
        if (player.getX() < player.getCurrentMap().getMatrixWidth() - 1)
            toCheck.add(new Pair<>(player.getLocationOffset(+1, 0), matrix[player.getX() + 1][player.getY()]));
        if (player.getX() > 0)
            toCheck.add(new Pair<>(player.getLocationOffset(0, -1), matrix[player.getX()][player.getY() - 1]));
        if (player.getY() < player.getCurrentMap().getMatrixHeight() - 1)
            toCheck.add(new Pair<>(player.getLocationOffset(0, +1), matrix[player.getX()][player.getY() + 1]));

        return toCheck.stream()
                .min(Comparator.comparingInt(Pair::getValue1))
                .get().getValue0();
    }
}