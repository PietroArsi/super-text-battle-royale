package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.players.Player;

public class WeaponMelee extends Weapon {

    public WeaponMelee(Weapon in, Player player) throws JsonLoadFailException {
        super(in.getSettingsFolder());
        this.player = player;
    }

}
