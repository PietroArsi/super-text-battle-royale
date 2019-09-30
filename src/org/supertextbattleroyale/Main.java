package org.supertextbattleroyale;

import org.supertextbattleroyale.exceptions.GameLoadException;
import org.supertextbattleroyale.game.GameLauncher;

public class Main {

    /**
     * Creates a new GameLauncher instance which loads the Window and the Game
     */
    public static void main(String[] args) throws GameLoadException {
        GameLauncher.loadWindow();
        GameLauncher.launchGame();
    }
}
