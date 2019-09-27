package org.supertextbattleroyale.players;

import org.supertextbattleroyale.players.statuses.Status;

@FunctionalInterface
public interface StatusAction {
    Status apply();
}
