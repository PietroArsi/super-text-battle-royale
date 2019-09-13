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

    private final File settingsFolder;

    private BufferedImage icon;

    private int maxHitPoints;
    private int hitPoints;
    private int level;

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
        this.equippedArmor = null;
        this.equippedWeapon = null;
        this.equippedPotions = new ArrayList<>();
    }

    public Player(Player in) throws JsonLoadFailException {
        this(in.settingsFolder);
    }

    public int getDamageVsPlayer(Weapon weapon, Player player) {
        //TODO: finish
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
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

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    };

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
        if(potion.getRemainingUses() > 0) {
            return potion.healPlayer(this);
        }
        else return 0;
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

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(this.icon,
                this.currentMap.X_DIST + this.x * this.currentMap.CELL_WIDTH + (int) (this.currentMap.CELL_WIDTH * 0.1),
                this.currentMap.Y_DIST + this.y * this.currentMap.CELL_HEIGHT + (int) (this.currentMap.CELL_HEIGHT * 0.1),
                this.currentMap.CELL_WIDTH - (int) (this.currentMap.CELL_WIDTH * 0.2),
                this.currentMap.CELL_HEIGHT - (int) (this.currentMap.CELL_HEIGHT * 0.2),
                null);

        if(this.equippedArmor != null) this.equippedArmor.draw(g);
    }
}
