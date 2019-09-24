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
        this.acquireInfo();
        player.decrementActionsLeft(1);

        List<Player> players = player.getPlayersSeen();

        if (!players.isEmpty()) {
            boolean wantsFight = players.stream().anyMatch(p -> player.wantsFight(p));

            if (wantsFight) {
                return new Combat(player);
            } else {
                return new Flee(player);
            }
        } else {
            return new Movement(player, player.getBestObjectiveOrMapCenter());
        }
    }

    private void acquireInfo() {
        MapUtils.getAllTilesFromType(player.getCurrentMap(), Door.class).stream()
                .filter(player::canSeeTile)
                .forEach(p -> player.getKnownPlaces().add(new Pair<>(player.getCurrentMap().getTileAt(p), p)));

        MapUtils.getAllTilesFromType(player.getCurrentMap(), Chest.class).stream()
                .filter(player::canSeeTile)
                .forEach(p -> player.getKnownPlaces().add(new Pair<>(player.getCurrentMap().getTileAt(p), p)));
    }
}