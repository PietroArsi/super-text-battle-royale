package org.supertextbattleroyale.players;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.statuses.Status;
import org.supertextbattleroyale.utils.ColorUtils;
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
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Drawable {

    @Setting
    private String name;

    @Setting
    private String alias;

    private final File settingsFolder;

    private BufferedImage body, face, underFace, overFace;

    private int maxHitPoints;
    private int hitPoints;
    private int level;
    private int XP;

    private Armor equippedArmor;
    private Weapon equippedWeapon;
    private List<Potion> equippedPotions;

    private Status currentStatus;
    private int actionsLeft;

    private GameMap currentMap;

    private int x, y;

    private List<Pair<Tile, Point>> knownPlaces;

    public Player(File settingsFolder) throws JsonLoadFailException {
        this.settingsFolder = settingsFolder;
        this.overFace = null;
        this.underFace = null;
        this.body = null;

        this.setupFromJson(new File(settingsFolder, "config.json"));
        this.setupIcon(settingsFolder);
        this.maxHitPoints = 100;
        this.hitPoints = this.maxHitPoints;
        this.level = 1;
        this.XP = 0;
        this.equippedArmor = null;
        this.equippedWeapon = null;
        this.equippedPotions = new ArrayList<>();
        this.knownPlaces = new ArrayList<>();

        if (this.body != null) {
            Color bodyColor = ColorUtils.makeColorGradient(
                    0.3f, 0.3f, 0.3f,
                    0, 2, 4,
                    RandomUtils.randomIntRange(0, 100), 128, 127);

            this.body = ColorUtils.tintImage(this.body, bodyColor);
        }
    }

    public Player(Player in) throws JsonLoadFailException {
        this(in.settingsFolder);
    }

    public void onTick() {
        //to get players in the same map getCurrentMap().getPlayersOnMap(
        this.actionsLeft = 2;


        while(this.actionsLeft > 0) {
            this.currentStatus = this.currentStatus.doStatusAction();
        }
    }

    public int getDamageVsPlayer(Weapon weapon, Player player) {
        //TODO: finish
        return 0;
    }

    public boolean wantsFight(Player p) {
        return RandomUtils.flipACoin() == 1;
    }

    public List<Player> getPlayersSeen() {
        return this.getCurrentMap().getPlayersOnMap().stream()
                .filter(p -> p != this)
                .filter(p -> p.canSeeTile(p.getLocation()))
                .collect(Collectors.toList());
    }

    /**
     * @param damage The damage done to the player and to the armor
     * @return The amount of effective damage done to the  player
     */
    public int receiveDamage(int damage) {
        int playerDamage;
        if (this.equippedArmor != null) {
            playerDamage = this.equippedArmor.getMitigatedDamage(damage);
            this.equippedArmor.damageArmor(damage - playerDamage);
        } else {
            playerDamage = damage;
        }
        this.hitPoints = Math.max(this.hitPoints - damage, 0);
        return playerDamage;
    }

    public int hitPlayer(Player other, int damage) {
        this.setXP(this.XP + damage); //XP are increased by the total damage done by the player
        return other.receiveDamage(damage);
    }

    public boolean canSeeTile(Point tileCoords) {
        ArrayList<Point> rayList = MapUtils.discretizeRay(this.getCurrentMap(), this.getLocation(), tileCoords);
        for (Point p : rayList) {
            if (!this.getCurrentMap().getMatrixMap()[p.x][p.y].isTileTransparent())
                return false;
        }
        return true;
    }

    /**
     * Gets if a player can walk on a given coordinate
     *
     * @param tx
     * @param ty
     * @return true if the tile is walkable and no other entity is on it
     */
    public boolean canWalkOnTile(int tx, int ty) {
        if (!this.getCurrentMap().getMatrixMap()[tx][ty].isTileWalkable()) return false;

        return this.getCurrentMap().getPlayersOnMap().stream()
                .anyMatch(p -> p != this && p.getX() == tx && p.getY() == ty);
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getActionsLeft() {
        return this.actionsLeft;
    }

    public void decrementActionsLeft(int amount) {
        this.actionsLeft -= amount;
    }

    public void move(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x < 0 || (getCurrentMap() != null && x >= getCurrentMap().getMatrixMap().length)) return;

        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y < 0 || (getCurrentMap() != null && y >= getCurrentMap().getMatrixMap()[0].length)) return;

        this.y = y;
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public Point getLocationOffset(int a, int b) {
        return new Point(this.x + a, this.y + b);
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
        return this.face;
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

    public List<Pair<Tile, Point>> getKnownPlaces() {
        return knownPlaces;
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
            File face = new File(config, "face.png");
            File underFace = new File(config, "underFace.png");
            File overFace = new File(config, "overFace.png");
            File body = new File(config, "body.png");

            this.face = ImageIO.read(new FileInputStream(face));
            if (underFace.exists()) this.underFace = ImageIO.read(new FileInputStream(underFace));
            if (overFace.exists()) this.overFace = ImageIO.read(new FileInputStream(overFace));
            if (body.exists()) this.body = ImageIO.read((new FileInputStream(body)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonLoadFailException();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        g.drawString(this.getAlias(), this.currentMap.X_DIST + this.x * this.currentMap.CELL_WIDTH, this.currentMap.Y_DIST + this.y * this.currentMap.CELL_HEIGHT);

        if (this.equippedArmor != null) this.equippedArmor.drawBack(g);

        if (this.underFace != null) this.drawImage(this.underFace, g);

        if (this.body != null) this.drawImage(this.body, g);

        this.drawImage(this.face, g);

        if (this.overFace != null) this.drawImage(this.overFace, g);

        if (this.equippedArmor != null) this.equippedArmor.draw(g);
        if (this.equippedWeapon != null) this.equippedWeapon.draw(g);
    }

    private void drawImage(BufferedImage image, Graphics2D g) {
//        g.translate(-this.currentMap.CELL_WIDTH / 2, -this.currentMap.CELL_HEIGHT);
        g.drawImage(image,
                this.currentMap.X_DIST + this.x * this.currentMap.CELL_WIDTH,
                this.currentMap.Y_DIST + this.y * this.currentMap.CELL_HEIGHT,
                this.currentMap.CELL_WIDTH,
                this.currentMap.CELL_HEIGHT,
                null);
//        g.translate(this.currentMap.CELL_WIDTH / 2, this.currentMap.CELL_HEIGHT);
    }

    public void vaiASeguireSistemiInformativi() {
        System.out.println("*Urla Internamente*");
        this.hitPoints -= 10;
    }
}
