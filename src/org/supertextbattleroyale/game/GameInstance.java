package org.supertextbattleroyale.game;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.items.WeaponMelee;
import org.supertextbattleroyale.items.WeaponRanged;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.text.Scoreboard;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstance {

    private GameMap currentMap;

    private List<Player> allPlayers;

    private Turn currentTurn;

    private Scoreboard s;

    public GameInstance() {
        this.allPlayers = new CopyOnWriteArrayList<>();

        this.s = new Scoreboard(500, 500);
    }

    //TODO: Get this working good
    public void initGame() {
        //Testing per il BFS
        this.setCurrentMap(GameLauncher.getLoadedMaps().get(1));
        ArrayList<Point> l = new ArrayList<>();
        l.add(new Point(0, 7));
        l.add(new Point(4, 0));
        MapUtils.printRoomMatrix(this.currentMap);
        MapUtils.printDistancesMatrix(this.currentMap, l);

        createRandomPlayer(3, 4, "Pit");
        createRandomPlayer(4, 1, "Dario");
        //        createRandomPlayer(5, 5, 2);
    }

    int count = 0;
    private void createRandomPlayer(int x, int y, String name) {
        try {
            Player player = GameLauncher.getPlayerFromName(name).get();
            Player p = new Player(player);
            this.getAllPlayers().add(p);

            List<Point> doors = MapUtils.getAllTilesFromType(getCurrentMap(), Door.class);
            p.move(doors.get(count));

            p.setCurrentMap(this.getCurrentMap());

            p.equipArmor(new Armor(GameLauncher.getLoadedArmors().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedArmors().size() - 1)), p));
            Weapon w = GameLauncher.getLoadedWeapons().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedWeapons().size() - 1));
            p.equipWeapon(w.isRanged() ? new WeaponRanged(w, p) : new WeaponMelee(w, p));
        } catch (JsonLoadFailException ex) {
            ex.printStackTrace();
        }
        count++;
    }

    int counter = 0;

    public void onTick() {
        int size = getCurrentMap().getAlivePlayersOnMap().size();

        if(size == 0) return;

        if(counter >= size) counter = 0;

        List<Player> p =  getCurrentMap().getAlivePlayersOnMap();
        p.size();
        getCurrentMap().getAlivePlayersOnMap().get(counter).onTick();

        counter++;
    }

    public void drawComponents(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameLauncher.getMainFrame().getGamePanel().getWidth(), GameLauncher.getMainFrame().getGamePanel().getHeight());

        this.getCurrentMap().draw(g);

        this.getAllPlayers().stream()
                .filter(player -> player.getCurrentMap() != null)
                .filter(player -> player.getCurrentMap().equals(currentMap))
                .filter(Player::isAlive)
                .sorted(Comparator.comparingInt(Player::getY))
                .forEach(player -> player.draw(g));

        this.s.draw(g);
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(GameMap map) {
        this.currentMap = map;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public Scoreboard getScoreboard() {
        return s;
    }
}
