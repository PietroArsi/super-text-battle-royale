package org.supertextbattleroyale.maps;

import org.supertextbattleroyale.interfaces.TileFilter;
import org.supertextbattleroyale.players.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Filters {
    public static TileFilter filterNonWalkableAndPlayers(Player skip) {
        return (aMap, aPoint) -> aMap
                .getPlayersOnMap().stream()
                .filter(p -> p != skip)
                .map(Player::getPoint)
                .noneMatch(p -> p.equals(aPoint)) && aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileWalkable();
    }

    public static TileFilter filterNonWalkableAndSeenPlayers(Player player) {
        return (aMap, aPoint) -> aMap
                .getPlayersOnMap().stream()
                .filter(p -> p != player)
                .filter(p -> player.getAlivePlayersSeen().contains(p))
                .map(Player::getLocation)
                .noneMatch(p -> p.equals(aPoint)) && aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileWalkable();
    }

    public static TileFilter filterNonWalkable() {
        return (aMap, aPoint) -> aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileWalkable();
    }

    public static TileFilter filterBulletProof() {
        return (aMap, aPoint) -> aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileBulletproof();
    }

    public static TileFilter filterOpaque() {
        return (aMap, aPoint) -> aMap.getMatrixMap()[aPoint.x][aPoint.y].isTileTransparent();
    }
}
