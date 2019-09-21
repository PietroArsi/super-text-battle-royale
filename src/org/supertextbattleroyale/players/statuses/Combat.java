package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

public class Combat extends Status {

    public Combat(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        if(this.player.getPlayersSeen().isEmpty()) return new Movement(this.player, this.player.getCurrentMap().getMapCenter());

        ((if(RandomUtils.bernoulli(this.))
    }
}