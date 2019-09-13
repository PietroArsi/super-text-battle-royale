package org.supertextbattleroyale.game;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstance {

    private GameMap currentMap;

    private List<Player> allPlayers, alivePlayers, deadPlayers;

    public final GameLauncher launcher;

    public GameInstance(GameLauncher launcher) {
        this.launcher = launcher;
        this.allPlayers = new CopyOnWriteArrayList<>();
        this.alivePlayers = new CopyOnWriteArrayList<>();
        this.deadPlayers = new CopyOnWriteArrayList<>();
    }

    //TODO: Get this working good
    public void initGame() {
        this.setCurrentMap(launcher.getLoadedMaps().get(1));

        for (Player player : launcher.getLoadedPlayers()) {
            try {
                Player p = new Player(player);
                this.getAllPlayers().add(p);
                this.getAlivePlayers().add(p);
                p.setX(new Random().nextInt(this.getCurrentMap().getMatrixMap().length));
                p.setY(new Random().nextInt(this.getCurrentMap().getMatrixMap()[0].length));
                p.setCurrentMap(this.getCurrentMap());

//                p.equipArmor(new Armor(launcher.getLoadedArmors().get(0), p));
                p.equipWeapon(new Weapon(launcher.getLoadedWeapons().get(0), p));
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onTick() {
        System.out.println("ontick");
        for (Player p : this.getAlivePlayers()) {
            p.setX(new Random().nextInt(this.getCurrentMap().getMatrixMap().length));
            p.setY(new Random().nextInt(this.getCurrentMap().getMatrixMap()[0].length));
        }
    }

    public void setCurrentMap(GameMap map) {
        this.currentMap = map;
    }

    public void drawComponents(Graphics2D g) {
        g.setColor(new Color(new Random().nextInt(10000000)));
        g.fillRect(0, 0, this.launcher.getMainFrame().getGamePanel().getWidth(), this.launcher.getMainFrame().getGamePanel().getHeight());

        this.getCurrentMap().draw(g);

        this.getAlivePlayers().stream()
                .filter(player -> player.getCurrentMap() != null && player.getCurrentMap().equals(currentMap))
                .forEach(player -> player.draw(g));
    }

    public GameMap getCurrentMap() {
        return currentMap;
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
