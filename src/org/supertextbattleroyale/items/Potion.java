package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.Setting;

import java.io.File;



public class Potion extends Collectible {
    @Setting
    private int maxHeal;    //Maximum hit points that the potion could heal
    @Setting
    private int maxUses;

    private int remainingUses;

    public Potion(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
        this.remainingUses = this.maxUses;
    }

    public int getMaxHeal()
    {
        return this.maxHeal;
    }

    public int getRemainingUses()
    {
        return this.remainingUses;
    }

    public int healPlayer(Player player)
    {
        this.remainingUses--;
        int oldHitPoints = player.getHitPoints();
        int newHitPoints = Math.min(oldHitPoints + this.maxHeal, player.getMaxHitPoints());
        player.setHitPoints(newHitPoints);
        return newHitPoints - oldHitPoints; //Return the number of HP actually healed
    }

}