package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.util.ArrayList;
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
            }
        }

        return null;
    }
}