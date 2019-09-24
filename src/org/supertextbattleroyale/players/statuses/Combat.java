package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
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
        player.acquireInfo();

        if (this.player.getPlayersSeen().isEmpty())
            return new Movement(this.player, this.player.getCurrentMap().getMapCenter());

        if (RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1) {    //TODO: Find a better random variabile
            handleFlee();
            Point objective = MapUtils.getBestObjective(player.getLocation(),
                    player.getCurrentMap(),
                    Filters.filterNonWalkableAndPlayers(),
                    MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class),
                    false);
            return new Flee(player);
        }

        if (!player.getEquippedPotions().isEmpty() && RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1) {
            player.usePotion(player.getEquippedPotions().get(0));
            return new Combat(player);
        }



        return null;
    }

    private void handleFlee() {
        //Get all visible doors
        List<Point> doors = player.getKnownPlaces().stream()
                .filter(pair -> pair.getValue0() instanceof Door)
                .map(Pair::getValue1).collect(Collectors.toList());

        int[][] distances = MapUtils.calculateDistances(player.getCurrentMap(), Filters.filterNonWalkableAndPlayers(), doors, false);

        Optional<Point> optNext;
        for(int i = 0; i < FLEE_DISTANCE; i++) {
            optNext  = MapUtils.getNextPathStep(player.getCurrentMap(),distances,player.getPoint(),false);
            if(optNext.isPresent()) {
                if(optNext.get().equals(player.getPoint())) player.warp();
                else player.move(optNext.get());

            }
        }

    }
}