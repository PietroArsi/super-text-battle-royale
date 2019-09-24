package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Flee extends Status {

    public Flee(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        return null;
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