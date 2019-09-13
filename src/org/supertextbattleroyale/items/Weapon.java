package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.Setting;

import java.awt.*;
import java.io.File;
import java.util.AbstractMap;

public class Weapon extends Collectible implements Drawable {

    @Setting
    private int baseDamage;

    @Setting
    private int level;

    private Player player;

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
        this(in.settingsFolder);
        this.player = player;
    }

    public AbstractMap.SimpleEntry<HitType, Integer> getHitStats() {
        HitType type = HitType.CRITIC;
        int damage = 100;
        int actualDamage = (int) Math.floor(type.damagePercentage * damage);

        return new AbstractMap.SimpleEntry<HitType, Integer>(type, actualDamage);
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(this.getImage(),
                player.getCurrentMap().X_DIST + player.getX() * player.getCurrentMap().CELL_WIDTH + (int) (player.getCurrentMap().CELL_WIDTH * 0.4),
                player.getCurrentMap().Y_DIST + player.getY() * player.getCurrentMap().CELL_HEIGHT + (int) (player.getCurrentMap().CELL_HEIGHT * 0.4),
                player.getCurrentMap().CELL_WIDTH - (int) (player.getCurrentMap().CELL_WIDTH * 0.2),
                player.getCurrentMap().CELL_HEIGHT - (int) (player.getCurrentMap().CELL_HEIGHT * 0.2),
                null);
    }

}
