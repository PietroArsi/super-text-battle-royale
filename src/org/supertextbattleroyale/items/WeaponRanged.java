package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.players.Player;

import java.io.File;

public class WeaponRanged extends Weapon {

    public WeaponRanged(Weapon in, Player player) throws JsonLoadFailException {
        super(in.getSettingsFolder());
        this.player = player;
    }

}
