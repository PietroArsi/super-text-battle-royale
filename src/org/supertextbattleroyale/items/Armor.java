package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.Setting;

import java.awt.*;
import java.io.File;

public class Armor extends Collectible implements Drawable {

    @Setting
    private float shieldPercentage;
    @Setting
    private int maximumHitPoints;

    private int hitPoints;
    private Player player; //Set only on copy

    public Armor(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
        //settings initialize maximumHitPoints, so on armor creation hitPoints need to be equal to maximum
        this.hitPoints = maximumHitPoints;
    }

    public Armor(Armor in, Player player) throws JsonLoadFailException {
        this(in.settingsFolder);
        this.player = player;
    }


    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void damageArmor(int hitPointsDamage) {
        this.hitPoints = Math.max(this.hitPoints -hitPointsDamage, 0);
    }

    public boolean isBroken() {
        return this.hitPoints <= 0;
    }

    public int getMitigatedDamage(int damage) {
        return damage - (int) Math.floor(damage * shieldPercentage);
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(this.getImage(),
                player.getCurrentMap().X_DIST + player.getX() * player.getCurrentMap().CELL_WIDTH + (int) (player.getCurrentMap().CELL_WIDTH * 0.05),
                player.getCurrentMap().Y_DIST + player.getY() * player.getCurrentMap().CELL_HEIGHT + (int) (player.getCurrentMap().CELL_HEIGHT * 0.05),
                player.getCurrentMap().CELL_WIDTH - (int) (player.getCurrentMap().CELL_WIDTH * 0.1),
                player.getCurrentMap().CELL_HEIGHT - (int) (player.getCurrentMap().CELL_HEIGHT * 0.1),
                null);
    }

}