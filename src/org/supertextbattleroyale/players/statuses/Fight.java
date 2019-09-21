package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

import java.util.ArrayList;

public class Fight extends Status {

    ArrayList<Player> fighters;
    Player current;

    public Fight(Type type) {
        super(type);
    }

    @Override
    public Status doStatusAction() {
        return null;
    }
}