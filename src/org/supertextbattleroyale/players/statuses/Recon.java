package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;

import java.util.List;

public class Recon extends Status {

    public Recon(Player player) {
        super(player);
    }

    @Override
    public StatusAction getStatusAction() {
        player.acquireInfo();
        player.decrementActionsLeft(1);
        getAttention();

        List<Player> players = player.getAlivePlayersSeen();

        if (!players.isEmpty()) {
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) {
                getAttention();

                return () -> new Combat(player);
            } else {
                getAttention();

                return () -> new Flee(player);
            }
        } else {
            return () -> new Movement(player, player.getBestObjectiveOrMapCenter());
        }
    }
}