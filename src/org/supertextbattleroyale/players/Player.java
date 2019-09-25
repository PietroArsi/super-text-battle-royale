package org.supertextbattleroyale.players;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Chest;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.statuses.Recon;
import org.supertextbattleroyale.players.statuses.Status;
import org.supertextbattleroyale.utils.*;

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

        if (this.currentStatus == null) {
            this.currentStatus = new Recon(this);
        }

//        while (this.actionsLeft > 0) {
        Status s = this.currentStatus.doStatusAction();
        this.currentStatus = s;
//        }
    }

    public int getDamageVsPlayer(Weapon weapon, Player player) {
        //TODO: finish
        return 0;
    }

    public float getDistanceToPlayer(Player other) {
        return (float) other.getLocation().distance(this.getLocation());
    }

    public boolean wantsFight(Player p) {
//        return false;
        return RandomUtils.flipACoin() == 1; //TODO
    }

    public List<Player> getPlayersSeen() {
        return this.getCurrentMap().getPlayersOnMap().stream()
                .filter(p -> p != this)
                .filter(p -> this.canSeeTile(p.getLocation()))
                .collect(Collectors.toList());

//        return Collections.emptyList();
    }

    public List<Player> getAlivePlayersSeen() {
        return this.getCurrentMap().getAlivePlayersOnMap().stream()
                .filter(p -> p != this)
                .filter(p -> this.canSeeTile(p.getLocation()))
                .collect(Collectors.toList());
    }

    public Optional<Player> findTargetPlayer() {
        return this.getAlivePlayersSeen().stream().min(Comparator.comparingDouble(this::getDistanceToPlayer));
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

    public void acquireInfo() {
        MapUtils.getAllTilesFromType(this.getCurrentMap(), Door.class).stream()
                .filter(this::canSeeTile)
                .filter(p-> !this.getKnownPlaces().contains(new Pair<>(this.getCurrentMap().getTileAt(p), p)))
                .forEach(p -> this.getKnownPlaces().add(new Pair<>(this.getCurrentMap().getTileAt(p), p)));

        MapUtils.getAllTilesFromType(this.getCurrentMap(), Chest.class).stream()
                .filter(this::canSeeTile)
                .filter(p-> !this.getKnownPlaces().contains(new Pair<>(this.getCurrentMap().getTileAt(p), p)))
                .forEach(p -> this.getKnownPlaces().add(new Pair<>(this.getCurrentMap().getTileAt(p), p)));
    }

    public Point getBestObjectiveOrMapCenter() {
        return getBestChest().orElseGet(() -> this.getCurrentMap().getMapCenter(Filters.filterNonWalkable()));
    }

    public Point getBestDoor() {
        List<Point> doors = this.getKnownPlaces().stream()
                .filter(pair -> pair.getValue0() instanceof Door)
                .map(Pair::getValue1)
                .collect(Collectors.toList());

        return MapUtils.getBestObjective(
                this.getLocation(),
                this.getCurrentMap(),
                Filters.filterNonWalkableAndPlayers(this),
                doors,
                false);
    }

    public Optional<Point> getBestChest() {
        List<Point> chests = this.getKnownPlaces().stream()
                .filter(pair -> pair.getValue0() instanceof Chest)
                .map(Pair::getValue1)
                .collect(Collectors.toList());

        if (chests.isEmpty()) return Optional.empty();

        return Optional.of(MapUtils.getBestObjective(
                this.getLocation(),
                this.getCurrentMap(),
                Filters.filterOpaque(),
                chests,
                false));
    }

    public Point getNextLocation(List<Point> destinations) {
        int[][] m = MapUtils.calculateDistances(
                this.getCurrentMap(),
                Filters.filterNonWalkableAndPlayers(this),
                destinations,
                false);

        Optional<Point> p =  MapUtils.getNextPathStep(
                this.getCurrentMap(),
                m,
                this.getLocation(),
                false);

        return p.orElse(this.getLocation());
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

    @Deprecated
    public void warp() {
        //TODO
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

    public Point getPoint() {
        return new Point(this.x, this.y);
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


    public boolean isAlive() {
        return this.hitPoints > 0;
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
        } else {
            this.equippedPotions.remove(potion);
            return 0;
        }
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
        g.setFont(new Font("Roboto", Font.BOLD, this.currentMap.CELL_WIDTH / 3));
        List<StringUtils.ColoredString> name = StringUtils.getLinesFromWidth(g, "^[dddddd]" + alias + "^[22ee33] " + hitPoints, this.currentMap.CELL_WIDTH * 4);
        int stringWidth = g.getFontMetrics().stringWidth(name.get(0).toString());

        g.fillRect(currentMap.X_DIST + x * currentMap.CELL_WIDTH + currentMap.CELL_WIDTH / 2 - stringWidth / 2 - 5,
                currentMap.Y_DIST + y * currentMap.CELL_HEIGHT - g.getFont().getSize(),
                stringWidth + 10,
                (int) (g.getFont().getSize() * 1.3f));
        StringUtils.drawCenteredColoredStringList(g, name,
                currentMap.X_DIST + x * currentMap.CELL_WIDTH + currentMap.CELL_WIDTH / 2,
                currentMap.Y_DIST + y * currentMap.CELL_HEIGHT);

        if (this.currentStatus != null) {
            List<StringUtils.ColoredString> status = StringUtils.getLinesFromWidth(g, "^[3344ff]" + currentStatus.getClass().getSimpleName(), this.currentMap.CELL_WIDTH * 4);
            StringUtils.drawCenteredColoredStringList(g, status,
                    currentMap.X_DIST + x * currentMap.CELL_WIDTH + currentMap.CELL_WIDTH / 2,
                    (int) (currentMap.Y_DIST + (y + 1.5f) * currentMap.CELL_HEIGHT));
        }
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
