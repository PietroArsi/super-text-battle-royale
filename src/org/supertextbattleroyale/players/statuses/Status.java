package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;

public abstract class Status {

    protected Player player;
    private boolean show;

    public Status(Player player) {
        this.player = player;
    }

    /**
     * Implement task
     *
     * @return the new status of the player
     */
    public abstract StatusAction getStatusAction();

    public boolean wantAttention() {
        return this.show;
    }

    public void getAttention() {
        this.show = true;
    }
}
