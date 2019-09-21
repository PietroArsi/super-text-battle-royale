package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

public class Flee extends Status {

    public Flee(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        return null;
    }
}