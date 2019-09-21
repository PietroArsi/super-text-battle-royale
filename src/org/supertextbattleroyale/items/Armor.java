package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.base.Collectible;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.Setting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Armor extends Collectible implements Drawable {

    @Setting
    private String name;

    @Setting
    private float shieldPercentage;
    @Setting
    private int maximumHitPoints;

    private int hitPoints;
    private Player player; //Set only on copy

    private BufferedImage back;

    public Armor(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
        //settings initialize maximumHitPoints, so on armor creation hitPoints need to be equal to maximum
        this.hitPoints = maximumHitPoints;
    }

    public Armor(Armor in, Player player) throws JsonLoadFailException {
        this(in.getSettingsFolder());
        this.player = player;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void damageArmor(int hitPointsDamage) {
        this.hitPoints = Math.max(this.hitPoints - hitPointsDamage, 0);
    }

    public boolean isBroken() {
        return this.hitPoints <= 0;
    }

    public int getMitigatedDamage(int damage) {
        return damage - (int) Math.floor(damage * shieldPercentage);
    }

    @Override
    public void draw(Graphics2D g) {
        this.drawImage(this.getImage(), g, this.player);
    }

    public void drawBack(Graphics2D g) {
        this.drawImage(this.back, g, this.player);
    }

    private void drawImage(BufferedImage image, Graphics2D g, Player p) {
        g.drawImage(image,
                p.getCurrentMap().X_DIST + p.getX() * p.getCurrentMap().CELL_WIDTH,
                p.getCurrentMap().Y_DIST + p.getY() * p.getCurrentMap().CELL_HEIGHT,
                p.getCurrentMap().CELL_WIDTH,
                p.getCurrentMap().CELL_HEIGHT,
                null);
    }

    @Override
    protected void setupIcon(File settingsFolder) throws JsonLoadFailException {
        super.setupIcon(settingsFolder);

        try {
            this.back = ImageIO.read(new FileInputStream(new File(settingsFolder, "back.png")));
        } catch (IOException e) {
        }
    }
}