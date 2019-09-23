package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

import java.awt.*;

public class Flee extends Status {

    private Point door;

    public Flee(Player player, Point door) {
        super(player);
        this.door = door;
    }

    @Override
    public Status doStatusAction() {
        return null;
    }
}