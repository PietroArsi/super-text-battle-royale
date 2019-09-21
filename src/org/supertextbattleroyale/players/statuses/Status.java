package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

public abstract class Status {

    protected Player player;

    public Status(Player player) {
        this.player = player;
    }

    /**
     * Implement task
     *
     * @return the new status of the player
     */
    public abstract Status doStatusAction();

}
