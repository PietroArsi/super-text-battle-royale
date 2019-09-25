package org.supertextbattleroyale.players.statuses;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.Filters;
import org.supertextbattleroyale.maps.MapUtils;
import org.supertextbattleroyale.maps.tiles.Chest;
import org.supertextbattleroyale.maps.tiles.Door;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Recon extends Status {

    public Recon(Player player) {
        super(player);
    }

    @Override
    public Status doStatusAction() {
        player.acquireInfo();
        player.decrementActionsLeft(1);

        List<Player> players = player.getAlivePlayersSeen();
        System.out.println("aaa");

        if (!players.isEmpty()) {
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            System.out.println("players");
            if (wantsFight) {
                return new Combat(player);
            } else {
                return new Flee(player);
            }
        } else {
            System.out.println("move");
            return new Movement(player, player.getBestObjectiveOrMapCenter());
        }
    }
}