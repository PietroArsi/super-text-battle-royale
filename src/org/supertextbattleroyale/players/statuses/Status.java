package org.supertextbattleroyale.players.statuses;

import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.players.StatusAction;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Status {

    protected Player player;
    private boolean show;

    private Point zoomCenter;
    private float zoomPercentage;

    public Status(Player player) {
        this.player = player;
    }

    /**
     * Implement task
     *
     * @return the new status of the player
     */
    public abstract StatusAction getStatusAction();

    public boolean wantAttention() {
        return this.show;
    }

    void getAttention(Player... players) {
        this.show = true;

        List<Player> playerList = new ArrayList<>(Arrays.asList(players));
        playerList.add(player);

        Point center = player.getLocation();
        float maxDistance = 0;

        for (Player p1 : players) {
            for (Player p2 : players) {
                if (p1 == p2) continue;

                float dist = (float) p1.getLocation().distance(p2.getLocation());
                if (dist > maxDistance) {
                    maxDistance = dist;
                    center = p1.getLocation();
                    center.translate((p1.getX() - p2.getX()) / 2, (p1.getX() - p2.getX()) / 2);
                }
            }
        }

        JPanel panel = GameLauncher.getMainFrame().getGamePanel();
        int dimension = Math.max(panel.getWidth(), panel.getHeight());
        float zoom = center.equals(player.getLocation()) ? 5 : dimension / maxDistance;

        //TODO: fix alignment

//                GameLauncher.getMainFrame().setupZoom(center.x, center.y, Math.max(1, Math.min(2, zoom)));


    }
}
