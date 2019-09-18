package org.supertextbattleroyale;

import org.supertextbattleroyale.exceptions.GameLoadException;
import org.supertextbattleroyale.game.GameLauncher;

public class Main {

    /**
     * Creates a new GameLauncher instance which loads the Window and the Game
     */
    public static void main(String[] args) {
        try {
            GameLauncher.loadWindow();
            GameLauncher.launchGame();
        } catch (GameLoadException ex) {
            ex.printStackTrace();
            System.out.println("Game failed to initialize, stopping.");
            System.exit(-1);
        }
    }
}
