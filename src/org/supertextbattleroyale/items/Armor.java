package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.utils.Setting;

import java.io.File;

public class Armor extends Collectible {

    @Setting
    private float shieldPercentage;
    @Setting
    private int maximumHitPoints;

    private int hitPoints;

    public Armor(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
        //settings initialize maximumHitPoints, so on armor creation hitPoints need to be equal to maximum
        this.hitPoints = maximumHitPoints;
    }

    public boolean isBroken() {
        return this.hitPoints <= 0;
    }

    public int getMitigatedDamage(int damage) {
        return damage - (int) Math.floor(damage * shieldPercentage);
    }
}
