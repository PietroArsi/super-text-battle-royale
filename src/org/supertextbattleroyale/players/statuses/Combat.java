package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.Comparator;
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

        Status nextStatus = null;

        if (this.player.getAlivePlayersSeen().isEmpty())
            //No player found, go to the best objective
            nextStatus = new Movement(this.player, this.player.getCurrentMap().getMapCenter());
        else if (RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1) {    //TODO: Find a better random variabile
            //If the random number is 1 then flee
            handleFlee();
            nextStatus = new Flee(this.player);
        } else {
            boolean heals = RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1;
            if (!player.getEquippedPotions().isEmpty() && heals) {
                player.usePotion(player.getEquippedPotions().get(0));
                return new Combat(player);
            } else {
                Optional<Player> maybeAPlayer = this.player.findTargetPlayer();
                if (maybeAPlayer.isPresent() && maybeAPlayer.get().getDistanceToPlayer(this.player) <= this.player.getEquippedWeapon().getRange()) {
                    //TODO: if(this.player.getActionsLeft() > 1)
                    if (this.player.getActionsLeft() > 0) {
                        //TODO: Precise attack

                    } else {
                        //TODO: Rapid attack
                    }

                } else {

                }
            }
        }

        return nextStatus;
    }

    private void handleFlee() {
        //Get all visible doors
        List<Point> doors = player.getKnownPlaces().stream()
                .filter(pair -> pair.getValue0() instanceof Door)
                .map(Pair::getValue1).collect(Collectors.toList());

        int[][] distances = MapUtils.calculateDistances(player.getCurrentMap(), Filters.filterNonWalkableAndPlayers(), doors, false);

        Optional<Point> optNext;
        optNext = MapUtils.getNextPathStep(player.getCurrentMap(), distances, player.getPoint(), false);
        if (optNext.isPresent()) {
            if (optNext.get().equals(player.getPoint())) player.warp();
            else player.move(optNext.get());

        }

    }
}