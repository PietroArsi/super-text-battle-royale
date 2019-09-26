package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

import java.util.List;

public class Recon extends Status {

    public Recon(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        player.acquireInfo();
        player.decrementActionsLeft(1);

        List<Player> players = player.getAlivePlayersSeen();

        if (!players.isEmpty()) {
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) {
                return new Combat(player);
            } else {
                return new Flee(player);
            }
        } else {
            return new Movement(player, player.getBestObjectiveOrMapCenter());
        }
    }
}