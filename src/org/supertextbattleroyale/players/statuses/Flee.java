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

        Point door = getBestDoor();

        int[][] distances = MapUtils.calculateDistances(
                player.getCurrentMap(),
                Filters.filterNonWalkableAndPlayers(),
                Collections.singletonList(door),
                false);

        Point next = MapUtils.getNextPathStep(
                player.getCurrentMap(),
                distances,
                player.getLocation(),
                false).get();

        player.move(next);
        Tile current = player.getCurrentMap().getTileAt(next);
        player.decrementActionsLeft(1);

        if (current instanceof Door) {
            player.setCurrentMap(((Door) current).getNextMap());
            return new Recon(player);
        } else {
            return new Flee(player);
        }
    }

    private Point getBestDoor() {
        List<Point> doors = player.getKnownPlaces().stream()
                .filter(pair -> pair.getValue0() instanceof Door)
                .map(Pair::getValue1).collect(Collectors.toList());

        MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                .filter(player::canSeeTile)
                .forEach(doors::add);

        return MapUtils.getBestObjective(
                player.getLocation(),
                player.getCurrentMap(),
                Filters.filterNonWalkableAndPlayers(),
                doors,
                false);
    }
}