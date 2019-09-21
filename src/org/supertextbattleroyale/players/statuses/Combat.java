package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Combat extends Status {

    public Combat(Player player) {
        super(player);
    }

    public static int FLEE_DISTANCE = 3;

    @Override
    public Status doStatusAction() {
        if(this.player.getPlayersSeen().isEmpty()) return new Movement(this.player, this.player.getCurrentMap().getMapCenter());

        if(RandomUtils.bernoulli( 1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1) {
            //Get all visible doors
            List<Point> doors = player.getKnownPlaces().stream()
                    .filter(pair -> pair.getValue0() instanceof Door)
                    .map(Pair::getValue1).collect(Collectors.toList());

            int[][] distances = MapUtils.calculateDistances(player.getCurrentMap(), (aMap, aPoint) -> player.getCurrentMap()
                    .getPlayersOnMap().stream()
                    .map(p -> p.getPoint())
                    .collect(Collectors.toList())
                    .stream()
                    .noneMatch(p -> p.equals(aPoint)) && aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileWalkable(),
                    doors, false);

            for(int i = 0; i < FLEE_DISTANCE; i++) {
                MapUtils.getNextPathStep(player.getCurrentMap(),distances,player.getPoint(),false);
                //TODO: Controllare l'optional
            }



        }


    }
}