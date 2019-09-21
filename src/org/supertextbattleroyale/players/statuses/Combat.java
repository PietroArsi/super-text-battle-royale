package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

public class Combat extends Status {

    public Combat(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        return null;
    }
}