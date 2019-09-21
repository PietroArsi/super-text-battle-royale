package org.supertextbattleroyale.game;

import org.supertextbattleroyale.players.Player;

public class Turn {

    private final GameInstance gameInstance;

    public Turn (GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void onTurn() {
//        gameInstance.getAlivePlayers().forEach(Player::onTick);
    }

}
