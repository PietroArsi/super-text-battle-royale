package org.supertextbattleroyale.game;

import org.javatuples.Pair;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.items.WeaponMelee;
import org.supertextbattleroyale.items.WeaponRanged;
import org.supertextbattleroyale.maps.GameMap;
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

    private final GameLauncher launcher;

    public GameInstance(GameLauncher launcher) {
        this.launcher = launcher;
        this.allPlayers = new CopyOnWriteArrayList<>();
        this.alivePlayers = new CopyOnWriteArrayList<>();
        this.deadPlayers = new CopyOnWriteArrayList<>();
    }

    //TODO: Get this working good
    public void initGame() {
        //Testing per il BFS
        this.setCurrentMap(launcher.getLoadedMaps().get(1));
        ArrayList<Pair<Integer,Integer>> l = new ArrayList<>();
        l.add(new Pair<>(0,4));
        l.add(new Pair<>(4,0));
        this.currentMap.printRoomMatrix();
        this.currentMap.printDistancesMatrix(l);

        for (Player player : launcher.getLoadedPlayers()) {
            try {
                Player p = new Player(player);
                this.getAllPlayers().add(p);
                this.getAlivePlayers().add(p);
                this.getCurrentMap().getPlayersOnMap().add(p);
                p.setX(new Random().nextInt(this.getCurrentMap().getMatrixMap().length));
                p.setY(new Random().nextInt(this.getCurrentMap().getMatrixMap()[0].length));
                p.setCurrentMap(this.getCurrentMap());

                p.equipArmor(new Armor(launcher.getLoadedArmors().get(RandomUtils.randomIntRange(0, launcher.getLoadedArmors().size() -1)), p));
                Weapon w = launcher.getLoadedWeapons().get(0);

                p.equipWeapon(w.isRanged() ? new WeaponRanged(w, p) : new WeaponMelee(w, p));
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onTick() {
        this.getCurrentMap().getPlayersOnMap().forEach(Player::onTick);
    }

    public void drawComponents(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.launcher.getMainFrame().getGamePanel().getWidth(), this.launcher.getMainFrame().getGamePanel().getHeight());

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
