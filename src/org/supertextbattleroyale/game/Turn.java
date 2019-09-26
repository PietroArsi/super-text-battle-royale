package org.supertextbattleroyale.game;

import org.supertextbattleroyale.players.Player;

import java.util.Timer;

public class Turn {

    private final GameInstance gameInstance;

    public Turn (GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    int counter = 0;

    public void onTurn() {
//        int size = gameInstance.getCurrentMap().getAlivePlayersOnMap().size();
//
//        if(size == 0) return;
//
//        System.out.println("B" + counter);
//
//        if(counter >= size) counter = 0;
//
//        System.out.println(counter);
//
//        gameInstance.getCurrentMap().getAlivePlayersOnMap().get(counter).onTick();
//
//        counter++;
    }

}
