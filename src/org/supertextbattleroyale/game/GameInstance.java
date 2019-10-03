package org.supertextbattleroyale.game;

import org.javatuples.Pair;
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
import org.supertextbattleroyale.utils.TimerUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstance {

    private GameMap currentMap;

    private List<Player> allPlayers;

    private Scoreboard s;

    public GameInstance() {
        this.allPlayers = new CopyOnWriteArrayList<>();

        this.s = new Scoreboard(500, 500);
    }

    //TODO: Get this working good
    public void initGame() {
        GameLauncher.getMainFrame().getGamePanel().pause();
        //Testing per il BFS
        this.setCurrentMap(GameLauncher.getLoadedMaps().get(1));
        ArrayList<Point> l = new ArrayList<>();
        l.add(new Point(0, 7));
        l.add(new Point(4, 0));
        MapUtils.printRoomMatrix(this.currentMap);
        MapUtils.printDistancesMatrix(this.currentMap, l);

        createRandomPlayer("Pit");
        createRandomPlayer("Dario");
        createRandomPlayer("Ari");
//        createRandomPlayer("Davide");
//        createRandomPlayer("Dani");
//        createRandomPlayer("Kien");
//        createRandomPlayer("Max");
//        createRandomPlayer("Paso");
//        createRandomPlayer("Giammi");

        this.setCurrentMap(allPlayers.get(0).getCurrentMap());
        GameLauncher.getMainFrame().getGamePanel().resume();
    }

    private List<Pair<GameMap, Point>> usedDoors = new ArrayList<>();

    private void createRandomPlayer(String name) {
        try {
            Optional<Player> player = GameLauncher.getPlayerFromName(name);
            if (player.isEmpty()) return;

            Player p = new Player(player.get());
            this.getAllPlayers().add(p);

            Pair<GameMap, Point> door;

            System.out.println("adding " + name + "...");

            do {
                GameMap currentMap = GameLauncher.getLoadedMaps().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedMaps().size() - 1));
                List<Point> doors = MapUtils.getAllTilesFromType(currentMap, Door.class);
                Point currentDoor = doors.get(RandomUtils.randomIntRange(0, doors.size() - 1));
                door = new Pair<>(currentMap, currentDoor);
            } while (usedDoors.contains(door));

            usedDoors.add(door);

            p.setCurrentMap(door.getValue0());
            p.move(door.getValue1());

            System.out.println("added " + name + " in " + door.getValue0().getName() + " " + door.getValue1());

            p.equipArmor(new Armor(GameLauncher.getLoadedArmors().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedArmors().size() - 1)), p));
            Weapon w = GameLauncher.getLoadedWeapons().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedWeapons().size() - 1));
            p.equipWeapon(w.isRanged() ? new WeaponRanged(w, p) : new WeaponMelee(w, p));
        } catch (JsonLoadFailException ex) {
            ex.printStackTrace();
        }
    }

    TimerUtils timer = new TimerUtils();

    private Queue<GameMap> maps = new ArrayDeque<>();
    private Queue<Player> playerQueue = new ArrayDeque<>();

    public void onTick() {
        if (maps.isEmpty()) {
            maps.addAll(GameLauncher.getLoadedMaps());
        }

        if (playerQueue.isEmpty()) {
            playerQueue.addAll(maps.poll().getAlivePlayersOnMap());
        }

        Player p = playerQueue.poll();
        if (p != null) {
            p.onTick();

            if(p.getCurrentMap() != getCurrentMap()) {
                this.onTick();
            }
        }


//        for (GameMap m : GameLauncher.getLoadedMaps()) {
//            for (Player player : m.getAlivePlayersOnMap()) {
////                if(timer.hasReach(1000)) {
//                player.onTick();
////                    timer.reset();
////                }
//            }
//        }
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
