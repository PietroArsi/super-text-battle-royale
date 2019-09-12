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

    private final File workingDirectory;
    private JFrame mainFrame;

    private List<Armor> armors;
    private List<Potion> potions;
    private List<Weapon> weapons;
    private List<Player> players;
    private List<GameMap> maps;

    public GameLauncher() {
        this.workingDirectory = new File(System.getProperty("user.dir"), "settings");

        this.armors = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
        this.players = new ArrayList<>();
        this.maps = new ArrayList<>();

        //TODO: this.getWeapons().get(0).getClass().getSuperclass();
    }

    public void loadWindow() {
        SwingUtilities.invokeLater(() -> {
            this.mainFrame = new GameWindow(this);
            this.setupFrame(this.mainFrame);
        });
    }

    public void launchGame() throws GameLoadException {
        this.loadArmors(new File(this.getWorkingDirectory(), "armors"));
        this.loadPotions(new File(this.getWorkingDirectory(), "potions"));
        this.loadWeapons(new File(this.getWorkingDirectory(), "weapons"));
        this.loadPlayers(new File(this.getWorkingDirectory(), "players"));
        this.loadMaps(new File(this.getWorkingDirectory(), "maps"));
    }

    private void loadArmors(File folder) throws GameLoadException {
        if(!folder.exists()) throw new GameLoadException();

        for (File armorFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.armors.add(new Armor(armorFolder));

                System.out.printf("Loaded '%s'\n", armorFolder.getName());
            }catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", armorFolder.getName());
            }
        }
    }

    private void loadPotions(File folder) throws GameLoadException  {
        if(!folder.exists()) throw new GameLoadException();

        for (File potionFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.potions.add(new Potion(potionFolder));

                System.out.printf("Loaded '%s'\n", potionFolder.getName());
            }catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", potionFolder.getName());
            }
        }
    }

    private void loadWeapons(File folder) throws GameLoadException {
        if(!folder.exists()) throw new GameLoadException();

        for (File weaponFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.weapons.add(new Weapon(weaponFolder));

                System.out.printf("Loaded '%s'\n", weaponFolder.getName());
            }catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", weaponFolder.getName());
            }
        }
    }

    private void loadPlayers(File folder) throws GameLoadException {
        if(!folder.exists()) throw new GameLoadException();

        for (File playerFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.players.add(new Player(playerFolder));

                System.out.printf("Loaded '%s'\n", playerFolder.getName());
            }catch (JsonLoadFailException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", playerFolder.getName());
            }
        }
    }

    private void loadMaps(File folder) throws GameLoadException {
        if(!folder.exists()) throw new GameLoadException();

        for (File mapFolder : Objects.requireNonNull(folder.listFiles())) {
            try {
                this.maps.add(new GameMap(mapFolder));

                System.out.printf("Loaded '%s'\n", mapFolder.getName());
            }catch (MapLoadException ex) {
                ex.printStackTrace();
                System.out.printf("Failed to load '%s'\n", mapFolder.getName());
            }
        }
    }

    private void setupFrame(JFrame frame) {
        frame.setSize(540, 650);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public File getWorkingDirectory() {
        return this.workingDirectory;
    }

    public List<Armor> getLoadedArmors() {
        return armors;
    }

    public List<Potion> getLoadedPotions() {
        return potions;
    }

    public List<Weapon> getLoadedWeapons() {
        return weapons;
    }

    public List<Player> getLoadedPlayers() {
        return players;
    }

    public List<GameMap> getLoadedMaps() {
        return maps;
    }

}
