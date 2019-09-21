package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Movement extends Status {

    public Movement(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        List<Player> players = player.getPlayersSeen();

        if (!players.isEmpty()) {
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) { //TODO: implementare una scelta piu' intelligente per la scelta del giocatore da combattere
                return new Combat(players.get(RandomUtils.randomIntRange(0, players.size() - 1)));
            } else {
                List<Point> doors = player.getKnownPlaces().stream()
                        .filter(pair -> pair.getValue0() instanceof Door)
                        .map(Pair::getValue1).collect(Collectors.toList());

                MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                        .filter(player::canSeeTile)
                        .forEach(doors::add);

                player.move(getNextPoint(doors));

                return new Flee(player, null); //TODO: get the door from calculate distances
            }
        } else {
            
        }

        return null;
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