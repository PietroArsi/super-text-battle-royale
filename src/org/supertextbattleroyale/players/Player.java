package org.supertextbattleroyale.players;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.utils.JsonUtils;
import org.supertextbattleroyale.utils.RandomUtils;
import org.supertextbattleroyale.utils.Setting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Player implements Drawable {

    @Setting
    private String name;

    @Setting
    private String alias;

    private final File settingsFolder;

    private BufferedImage icon;

    private int maxHitPoints;
    private int hitPoints;
    private int level;
    private int XP;

    private Armor equippedArmor;
    private Weapon equippedWeapon;
    private List<Potion> equippedPotions;

    private GameMap currentMap;

    private int x, y;

    public Player(File settingsFolder) throws JsonLoadFailException {
        this.settingsFolder = settingsFolder;

        this.setupFromJson(new File(settingsFolder, "config.json"));
        this.setupIcon(settingsFolder);
        this.maxHitPoints = 100;
        this.hitPoints = this.maxHitPoints;
        this.level = 1;
        this.XP = 0;
        this.equippedArmor = null;
        this.equippedWeapon = null;
        this.equippedPotions = new ArrayList<>();
    }

    public Player(Player in) throws JsonLoadFailException {
        this(in.settingsFolder);
    }

    public void onTick() {
        //to get players in the same map getCurrentMap().getPlayersOnMap()

        //test
        this.setX(RandomUtils.randomIntRange(this.getX() - 1, this.getX() + 1));
        this.setY(RandomUtils.randomIntRange(this.getY() - 1, this.getY() + 1));

        //TODO:
    }

    public int getDamageVsPlayer(Weapon weapon, Player player) {
        //TODO: finish
        return 0;
    }

    /**
     *
     * @param damage The damage done to the player and to the armor
     * @return The amount of effective damage done to the  player
     */
    public int receiveDamage(int damage) {
        int playerDamage;
        if(this.equippedArmor != null) {
            playerDamage = this.equippedArmor.getMitigatedDamage(damage);
            this.equippedArmor.damageArmor(damage - playerDamage);
        }
        else {
            playerDamage = damage;
        }
        this.hitPoints = Math.max(this.hitPoints - damage, 0);
        return playerDamage;
    }

    public int hitPlayer(Player other, int damage) {
        this.setXP(this.XP + damage); //XP are increased by the total damage done by the player
        return other.receiveDamage(damage);
    }

    /**
     * Gets if a player can walk on a given coordinate
     *
     * @param tx
     * @param ty
     * @return true if the tile is walkable and no other entity is on it
     */
    public boolean canWalkOnTile(int tx, int ty) {
        if(!this.getCurrentMap().getMatrixMap()[tx][ty].isTileWalkable()) return false;

        return this.getCurrentMap().getPlayersOnMap().stream()
                .anyMatch(p -> p != this && p.getX() == tx && p.getY() == ty);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        g.drawString(this.getAlias(), this.currentMap.X_DIST + this.x * this.currentMap.CELL_WIDTH, this.currentMap.Y_DIST + this.y * this.currentMap.CELL_HEIGHT);

        g.drawImage(this.icon,
                this.currentMap.X_DIST + this.x * this.currentMap.CELL_WIDTH + (int) (this.currentMap.CELL_WIDTH * 0.1),
                this.currentMap.Y_DIST + this.y * this.currentMap.CELL_HEIGHT + (int) (this.currentMap.CELL_HEIGHT * 0.1),
                this.currentMap.CELL_WIDTH - (int) (this.currentMap.CELL_WIDTH * 0.2),
                this.currentMap.CELL_HEIGHT - (int) (this.currentMap.CELL_HEIGHT * 0.2),
                null);

        if (this.equippedArmor != null) this.equippedArmor.draw(g);
        if (this.equippedWeapon != null) this.equippedWeapon.draw(g);
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if(x < 0 || (getCurrentMap() != null && x >= getCurrentMap().getMatrixMap().length)) return;

        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if(y < 0 || (getCurrentMap() != null && y >= getCurrentMap().getMatrixMap()[0].length)) return;

        this.y = y;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(GameMap currentMap) {
        this.currentMap = currentMap;
    }

    public int getLevel() {
        return this.level;
    }

    public int getXP() {
        return this.XP;
    }

    public void setXP(int XP) {
    }

    public int getHP() {
        return this.hitPoints;
    }

    public BufferedImage getImage() {
        return this.icon;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void equipArmor(Armor equippedArmor) {
        this.equippedArmor = equippedArmor;
    }

    public void equipWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public List<Potion> getEquippedPotions() {
        return equippedPotions;
    }

    public int usePotion(Potion potion) {
        if (potion.getRemainingUses() > 0) {
            return potion.healPlayer(this);
        } else return 0;
    }

    private void setupFromJson(File config) throws JsonLoadFailException {
        Optional<JsonElement> jsonElement = JsonUtils.getJsonElementFromFile(config);

        if (jsonElement.isEmpty()) throw new JsonLoadFailException();

        JsonObject jsonObject = jsonElement.get().getAsJsonObject();
        Gson gson = new Gson();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, gson.fromJson(entry.getValue(), field.getType()));
            } catch (NoSuchFieldException | IllegalAccessException exx) {
                exx.printStackTrace();

                throw new JsonLoadFailException();
            }
        }
    }

    private void setupIcon(File config) throws JsonLoadFailException {
        try {
            this.icon = ImageIO.read(new FileInputStream(new File(config, "icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonLoadFailException();
        }
    }
}
