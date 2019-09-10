package org.supertextbattleroyale;

import org.supertextbattleroyale.exceptions.GameLoadException;
import org.supertextbattleroyale.game.GameLauncher;

public class Main {

    public static void main(String[] args) {
        try {
            GameLauncher launcher = new GameLauncher();
            launcher.loadWindow();
            launcher.launchGame();
        }catch (GameLoadException ex) {
            ex.printStackTrace();
            System.out.println("Game failed to initialize, stopping.");
            System.exit(-1);
        }
    }

}
