package org.supertextbattleroyale.players;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.utils.JsonUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Player {

    private String name;
    private BufferedImage icon;

    private int hitPoints;
    private int level;

    private Armor equippedArmor;
    private Weapon equippedWeapon;
    private List<Potion> equippedPotions;

    public Player(File settingsFolder) throws JsonLoadFailException {
        this.setupFromJson(new File(settingsFolder, "config.json"));
        this.setupIcon(settingsFolder);

        this.hitPoints = 100;
        this.level = 1;
        this.equippedArmor = null;
        this.equippedWeapon = null;
        this.equippedPotions = new ArrayList<>();
    }

    public int getDamageVsPlayer(Weapon weapon, Player player) {
        return 0;
    }

    public String getName() {
        return this.name;
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

    public void setEquippedArmor(Armor equippedArmor) {
        this.equippedArmor = equippedArmor;
    }

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
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
