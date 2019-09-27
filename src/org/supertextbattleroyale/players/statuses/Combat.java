package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;
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
        return false;
//        return RandomUtils.bernoulli(1 - player.getHitPoints() / (2f * player.getMaxHitPoints())) == 1;
    }

    @Override
    public StatusAction getStatusAction() {
        player.acquireInfo();
        
        if (player.getAlivePlayersSeen().isEmpty())
            //No player found, go to the best objective
            return () -> new Movement(player, player.getMapCenter());
        else if (wantsFlee()) {
            getAttention();
            //If the random number is 1 then flee
            return () -> {
                handleFlee();

                return new Flee(player);
            };
        } else {
            //The probability of using a potion grows with the damage taken by the player
            boolean heals = RandomUtils.bernoulli(1 - player.getHitPoints() / (2f * player.getMaxHitPoints())) == 1;

            if (!player.getEquippedPotions().isEmpty() && heals) {
                getAttention();

                return () -> {
                    player.usePotion(player.getEquippedPotions().get(0));
                    player.decrementActionsLeft(1);

                    return new Combat(player);
                };
            } else {
                //Search if there is an alive player to fight
                Optional<Player> maybeAPlayer = player.findTargetPlayer();

                if (maybeAPlayer.isPresent()) {
                    if(isInRangeToAttack(maybeAPlayer.get())) {
                        if (player.getActionsLeft() > 1) {
                            getAttention();

                            //TODO: Precise attack
                            return () -> {
                                player.hitPlayer(maybeAPlayer.get(), (int) (player.getEquippedWeapon().getBaseDamage() * 1.3f));
                                player.decrementActionsLeft(2);
                                return new Combat(player);
                            };
                        } else {
                            getAttention();

                            //TODO: Rapid attack
                            return () -> {
                                player.hitPlayer(maybeAPlayer.get(), (int) (player.getEquippedWeapon().getBaseDamage()));
                                player.decrementActionsLeft(1);
                                return new Combat(player);
                            };
                        }
                    } else {
                        return () -> {
                            player.move(player.getNextLocation(Collections.singletonList(maybeAPlayer.get().getLocation())));

                            return new Combat(player);
                        };
                    }
                } else {
                    //Move to nearest interesting objective or else move to map center
//                    Point obj = player.getNextDestination();
//
//                    player.move(player.getNextLocation(Collections.singletonList(obj)));
//
//                    player.decrementActionsLeft(1);
//                    return new Movement(player, obj);
                    return () -> new Movement(player, player.getNextDestination());
                }
            }
        }
    }

    private boolean isInRangeToAttack(Player vs) {
        return player.getDistanceToPlayer(vs) <= player.getEquippedWeapon().getRange();
    }

    private void handleFlee() {
        //Get all visible doors
        Point door = player.getBestDoor();

        Point p = player.getNextLocation(Collections.singletonList(door));
        player.move(p);
    }
}