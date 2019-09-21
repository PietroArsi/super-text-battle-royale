package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Movement extends Status {

    public Movement(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {


        return null;
    }

    private List<Player> seenPlayers() {
        return player.getCurrentMap().getPlayersOnMap().stream()
                .filter(p -> p != this.player)
                .filter(p -> p.canSeeTile(p.getLocation()))
                .collect(Collectors.toList());
    }
}