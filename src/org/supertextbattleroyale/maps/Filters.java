package org.supertextbattleroyale.maps;

import org.supertextbattleroyale.interfaces.TileFilter;
import org.supertextbattleroyale.players.Player;

import java.util.stream.Collectors;

public class Filters {
    public static TileFilter filterNonWalkableAndPlayers() {
        return (aMap, aPoint) -> aMap
                .getPlayersOnMap().stream()
                .map(Player::getPoint)
                .collect(Collectors.toList())
                .stream()
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
