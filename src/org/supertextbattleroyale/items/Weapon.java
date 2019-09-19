package org.supertextbattleroyale.items;

import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.AbstractMap;

public class Weapon extends Collectible implements Drawable {

    @Setting
    private int baseDamage;

    @Setting
    private boolean isRanged;

    private int level;
    protected Player player;

    public enum HitType {
        CRITIC(2.0f),
        NORMAL(1.0f),
        PARTIAL(-1.0f), //random range from 0.5 to 0.75
        FAIL(0.0f);

        float damagePercentage;

        HitType(float damagePercentage) {
            this.damagePercentage = damagePercentage;
        }
    }

    public Weapon(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);

        this.level = 1;
    }

    public Weapon(Weapon in, Player player) throws JsonLoadFailException {
        this(in.getSettingsFolder());
        this.player = player;
    }

    public Pair<HitType, Integer> getHitStats() {
        HitType type = HitType.CRITIC;
        int damage = 100;
        int actualDamage = (int) Math.floor(type.damagePercentage * damage);

        return new Pair<>(type, actualDamage);
    }

    @Override
    public void draw(Graphics2D g) {
        this.drawImage(getImage(), g, player);
    }

    private void drawImage(BufferedImage image, Graphics2D g, Player p) {
        g.drawImage(image,
                p.getCurrentMap().X_DIST + p.getX() * p.getCurrentMap().CELL_WIDTH,
                p.getCurrentMap().Y_DIST + p.getY() * p.getCurrentMap().CELL_HEIGHT,
                p.getCurrentMap().CELL_WIDTH,
                p.getCurrentMap().CELL_HEIGHT,
                null);
    }

    public boolean isRanged() {
        return isRanged;
    }
}
