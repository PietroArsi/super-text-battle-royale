package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Combat extends Status {

    public Combat(Player player) {
        super(player);
    }

    public static int FLEE_DISTANCE = 3;

    //TODO: Find a better random variabile
    private boolean wantsFlee() {
//        System.out.println(RandomUtils.exponential(1));
        return false;
//        return RandomUtils.bernoulli(1 - this.player.getHitPoints() / (2f * this.player.getMaxHitPoints())) == 1;
    }

    @Override
    public Status doStatusAction() {
        player.acquireInfo();

        Status nextStatus;

        if (this.player.getAlivePlayersSeen().isEmpty())
            //No player found, go to the best objective
            nextStatus = new Movement(this.player, this.player.getCurrentMap().getMapCenter(Filters.filterNonWalkable()));
        else if (wantsFlee()) {
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
                if (maybeAPlayer.isPresent()) {
                    if(maybeAPlayer.get().getDistanceToPlayer(this.player) <= this.player.getEquippedWeapon().getRange()) {
                        if (this.player.getActionsLeft() > 1) {
                            //TODO: Precise attack
                            this.player.hitPlayer(maybeAPlayer.get(), (int) (this.player.getEquippedWeapon().getBaseDamage() * 1.3f));
                            this.player.decrementActionsLeft(2);
                        } else {
                            //TODO: Rapid attack
                            this.player.hitPlayer(maybeAPlayer.get(), (int) (this.player.getEquippedWeapon().getBaseDamage()));
                            this.player.decrementActionsLeft(1);
                        }
                    } else {
                        player.move(player.getNextLocation(Collections.singletonList(maybeAPlayer.get().getLocation())));
                    }
                    nextStatus = new Combat(player);
                } else {
                    //Move to nearest interesting objective or else move to map center
                    Point obj = player.getNextDestination();

                    player.move(player.getNextLocation(Collections.singletonList(obj)));

                    this.player.decrementActionsLeft(1);
                    return new Movement(player, obj);
                }
            }
        }

        return nextStatus;
    }

    private void handleFlee() {
        //Get all visible doors
        Point door = player.getBestDoor();

        Point p = player.getNextLocation(Collections.singletonList(door));
        player.move(p);
    }
}