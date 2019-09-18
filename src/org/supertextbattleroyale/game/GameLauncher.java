package org.supertextbattleroyale.game;

import org.supertextbattleroyale.exceptions.GameLoadException;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.exceptions.MapLoadException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.maps.GameMap;
import org.supertextbattleroyale.players.Player;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameLauncher {

    private static GameLauncher instance = new GameLauncher();

    private final File workingDirectory;
    private GameWindow mainFrame;

    private List<Armor> armors;
    private List<Potion> potions;
    private List<Weapon> weapons;
    private List<Player> players;
    private List<GameMap> maps;

    private static GameInstance gameInstance;

    public GameLauncher() {
        this.workingDirectory = new File(System.getProperty("user.dir"), "settings");

        this.armors = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
        this.players = new ArrayList<>();
        this.maps = new ArrayList<>();

        //TODO: this.getWeapons().get(0).getClass().getSuperclass();
    }

    /**
     * Starts the window rendering
     */
    public static void loadWindow() {
        SwingUtilities.invokeLater(() -> {
            getInstance().mainFrame = new GameWindow();
            getInstance().setupFrame(getMainFrame());
        });
    }

    public static void launchGame() throws GameLoadException {
        getInstance().loadArmors(new File(getWorkingDirectory(), "armors"));
        getInstance().loadPotions(new File(getWorkingDirectory(), "potions"));
        getInstance().loadWeapons(new File(getWorkingDirectory(), "weapons"));
        getInstance().loadPlayers(new File(getWorkingDirectory(), "players"));

        try {
            GameMap.loadTileTypes(new File(getWorkingDirectory(), "maps/tiles.json"));
        } catch (JsonLoadFailException ex) {
            ex.printStackTrace();
            throw new GameLoadException();
        }

        getInstance().loadMaps(new File(getWorkingDirectory(), "maps"));

        gameInstance = new GameInstance();
        gameInstance.initGame();
    }

    private void loadArmors(File folder) throws GameLoadException {
        if (!folder.exists()) throw new GameLoadException();

        for (File armorFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.armors.add(new Armor(armorFolder));

                System.out.printf("Loaded '%s'\n", armorFolder.getName());
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", armorFolder.getName());
            }
        }
    }

    private void loadPotions(File folder) throws GameLoadException {
        if (!folder.exists()) throw new GameLoadException();

        for (File potionFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.potions.add(new Potion(potionFolder));

                System.out.printf("Loaded '%s'\n", potionFolder.getName());
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", potionFolder.getName());
            }
        }
    }

    private void loadWeapons(File folder) throws GameLoadException {
        if (!folder.exists()) throw new GameLoadException();

        for (File weaponFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.weapons.add(new Weapon(weaponFolder));

                System.out.printf("Loaded '%s'\n", weaponFolder.getName());
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", weaponFolder.getName());
            }
        }
    }

    private void loadPlayers(File folder) throws GameLoadException {
        if (!folder.exists()) throw new GameLoadException();

        for (File playerFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.players.add(new Player(playerFolder));

                System.out.printf("Loaded '%s'\n", playerFolder.getName());
            } catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", playerFolder.getName());
            }
        }
    }

    private void loadMaps(File folder) throws GameLoadException {
        if (!folder.exists()) throw new GameLoadException();

        for (File mapFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                if (!mapFolder.isDirectory()) continue;

                this.maps.add(new GameMap(mapFolder));

                System.out.printf("Loaded '%s'\n", mapFolder.getName());
            } catch (MapLoadException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", mapFolder.getName());
            }
        }
    }

    private void setupFrame(JFrame frame) {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
    }

    public static GameInstance getGameInstance() {
        return getInstance().gameInstance;
    }

    public static File getWorkingDirectory() {
        return getInstance().workingDirectory;
    }

    public static List<Armor> getLoadedArmors() {
        return getInstance().armors;
    }

    public static List<Potion> getLoadedPotions() {
        return getInstance().potions;
    }

    public static List<Weapon> getLoadedWeapons() {
        return getInstance().weapons;
    }

    public static List<Player> getLoadedPlayers() {
        return getInstance().players;
    }

    public static List<GameMap> getLoadedMaps() {
        return getInstance().maps;
    }

    public static GameWindow getMainFrame() {
        return getInstance().mainFrame;
    }

    public static GameLauncher getInstance() {
        return instance;
    }
}
