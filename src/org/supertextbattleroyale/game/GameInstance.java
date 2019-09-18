package org.supertextbattleroyale.game;

import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.items.WeaponMelee;
import org.supertextbattleroyale.items.WeaponRanged;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstance {

    private GameMap currentMap;

    private List<Player> allPlayers, alivePlayers, deadPlayers;

    private Turn currentTurn;

    public GameInstance() {
        this.allPlayers = new CopyOnWriteArrayList<>();
        this.alivePlayers = new CopyOnWriteArrayList<>();
        this.deadPlayers = new CopyOnWriteArrayList<>();
    }

    //TODO: Get this working good
    public void initGame() {
        //Testing per il BFS
        this.setCurrentMap(GameLauncher.getLoadedMaps().get(1));
        ArrayList<Pair<Integer, Integer>> l = new ArrayList<>();
        l.add(new Pair<>(0, 4));
        l.add(new Pair<>(4, 0));
        MapUtils.printRoomMatrix(this.currentMap);
        MapUtils.printDistancesMatrix(this.currentMap, l);

        for (Player player : GameLauncher.getLoadedPlayers()) {
            try {
                Player p = new Player(player);
                this.getAllPlayers().add(p);
                this.getAlivePlayers().add(p);
                this.getCurrentMap().getPlayersOnMap().add(p);
                p.setX(RandomUtils.randomIntRange(0, this.getCurrentMap().getMatrixMap().length -1));
                p.setY(RandomUtils.randomIntRange(0, this.getCurrentMap().getMatrixMap()[0].length -1));
                p.setCurrentMap(this.getCurrentMap());

                p.equipArmor(new Armor(GameLauncher.getLoadedArmors().get(RandomUtils.randomIntRange(0, GameLauncher.getLoadedArmors().size() - 1)), p));
                Weapon w = GameLauncher.getLoadedWeapons().get(0);

                p.equipWeapon(w.isRanged() ? new WeaponRanged(w, p) : new WeaponMelee(w, p));
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onTick() {
//        GameWindow w = this.launcher.getMainFrame();
//        w.setupZoom(w.getWidth()/RandomUtils.randomIntRange(1, 10), w.getHeight()/RandomUtils.randomIntRange(1, 10), RandomUtils.randomIntRange(1, 4));
        this.currentTurn = new Turn(this);
        this.currentTurn.onTurn();
    }

    public void drawComponents(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameLauncher.getMainFrame().getGamePanel().getWidth(), GameLauncher.getMainFrame().getGamePanel().getHeight());

        this.getCurrentMap().draw(g);

        this.getAlivePlayers().stream()
                .filter(player -> player.getCurrentMap() != null && player.getCurrentMap().equals(currentMap))
                .sorted(Comparator.comparingInt(Player::getY))
                .forEach(player -> player.draw(g));
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

    public List<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public List<Player> getDeadPlayers() {
        return deadPlayers;
    }
}
