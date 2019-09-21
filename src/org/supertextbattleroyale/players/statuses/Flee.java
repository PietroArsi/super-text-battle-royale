package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.players.Player;

import java.awt.*;

public class Flee extends Status {

<<<<<<< HEAD
    private Point door;

    public Flee(Player player, Point door) {
=======


    public Flee(Player player) {
>>>>>>> c3b19c276b03286fe923f4d36c55e55877832cfd
        super(player);
        this.door = door;
    }

    @Override
    public Status doStatusAction() {
        return null;
    }
}