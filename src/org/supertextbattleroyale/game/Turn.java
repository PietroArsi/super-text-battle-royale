package org.supertextbattleroyale.game;

import org.supertextbattleroyale.players.Player;

public class Turn {

    private final GameInstance gameInstance;

    public Turn (GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void onTurn() {
        gameInstance.getCurrentMap().getAlivePlayersOnMap().forEach(Player::onTick);
//        gameInstance.getCurrentMap().getPlayersOnMap().get(0).setX(7);
//        gameInstance.getCurrentMap().getPlayersOnMap().get(0).setY(2);
//
//        gameInstance.getCurrentMap().getPlayersOnMap().get(1).setX(11);
//        gameInstance.getCurrentMap().getPlayersOnMap().get(1).setY(7);
//
//        gameInstance.getCurrentMap().getPlayersOnMap().forEach(Player::onTick);
//        gameInstance.getAlivePlayers().forEach(Player::onTick);
    }

}
