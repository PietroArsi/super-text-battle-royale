package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
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
            nextStatus = new Movement(this.player, this.player.getCurrentMap().getMapCenter(Filters.filterNonWalkableAndPlayers(player)));
        else if (RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1) {    //TODO: Find a better random variabile
            //If the random number is 1 then flee
            handleFlee();
            nextStatus = new Flee(this.player);
        } else {
            //The probability of using a potion grows with the damage taken by the player
            boolean heals = RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1;
            if (!player.getEquippedPotions().isEmpty() && heals) {
                player.usePotion(player.getEquippedPotions().get(0));
                this.player.decrementActionsLeft(1);
                nextStatus = new Combat(player);
            } else {
                //Search if there is an alive player to fight
                Optional<Player> maybeAPlayer = this.player.findTargetPlayer();
                if (maybeAPlayer.isPresent() && maybeAPlayer.get().getDistanceToPlayer(this.player) <= this.player.getEquippedWeapon().getRange()) {
                    if (this.player.getActionsLeft() > 1) {
                        //TODO: Precise attack
                        this.player.hitPlayer(maybeAPlayer.get(), (int) (this.player.getEquippedWeapon().getBaseDamage() * 1.3f));
                        this.player.decrementActionsLeft(2);

                    } else {
                        //TODO: Rapid attack
                        this.player.hitPlayer(maybeAPlayer.get(), (int) (this.player.getEquippedWeapon().getBaseDamage()));
                        this.player.decrementActionsLeft(1);
                    }
                    nextStatus = new Combat(this.player);
                } else {
                    //Find interesting tiles in the map
                    Point obj = MapUtils.getBestObjective(this.player.getLocation(), this.player.getCurrentMap(), Filters.filterNonWalkableAndPlayers(player),
                            this.player.getAlivePlayersSeen().stream()
                                    .map(Player::getLocation).collect(Collectors.toList()), false);
                    ArrayList<Point> objl = new ArrayList<>();
                    objl.add(obj);
                    //Move to nearest interesting objective or else move to map center
                    player.move(MapUtils.getNextPathStep(this.player.getCurrentMap(),
                            MapUtils.calculateDistances(this.player.getCurrentMap(), Filters.filterNonWalkableAndPlayers(player), objl, false),
                            player.getLocation(), false).orElseGet(() -> this.player.getCurrentMap().getMapCenter(Filters.filterNonWalkableAndPlayers(player))));
                    this.player.decrementActionsLeft(1);
                    nextStatus = new Movement(player, obj);
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

        int[][] distances = MapUtils.calculateDistances(player.getCurrentMap(), Filters.filterNonWalkableAndPlayers(player), doors, false);

        Optional<Point> optNext;
        optNext = MapUtils.getNextPathStep(player.getCurrentMap(), distances, player.getPoint(), false);
        if (optNext.isPresent()) {
            if (optNext.get().equals(player.getPoint())) player.warp();
            else player.move(optNext.get());

        }

    }
}