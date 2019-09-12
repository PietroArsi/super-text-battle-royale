package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;

import java.io.File;
import java.util.AbstractMap;

public class Weapon extends Collectible {

    private int baseDamage;
    private int level;

    public enum HitType {
        CRITIC(2.0f),
        NORMAL(1.0f),
        PARTIAL(-1.0f), //random range from 0.5 to 0.75
        FAIL(0.0f);

        float damagePercentage;

        HitType (float damagePercentage) {
            this.damagePercentage = damagePercentage;
        }
    }

    public Weapon(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);

        this.level = 1;
    }

    public AbstractMap.SimpleEntry<HitType, Integer> getHitStats() {
        HitType type = HitType.CRITIC;
        int damage = 100;
        int actualDamage = (int) Math.floor(type.damagePercentage * damage);

        return new AbstractMap.SimpleEntry<HitType, Integer>(type, actualDamage);
    }
}
