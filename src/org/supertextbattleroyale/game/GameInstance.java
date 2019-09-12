package org.supertextbattleroyale.game;

import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.util.List;
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

    public void setCurrentMap(GameMap map) {
        this.currentMap = map;
    }

    public void drawComponents(Graphics2D g) {
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
