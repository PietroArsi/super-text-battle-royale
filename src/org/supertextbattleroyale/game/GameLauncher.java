package org.supertextbattleroyale.game;

import org.supertextbattleroyale.exceptions.GameLoadException;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.Armor;
import org.supertextbattleroyale.items.Potion;
import org.supertextbattleroyale.items.Weapon;
import org.supertextbattleroyale.players.Player;

import javax.swing.*;
import java.awt.*;
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



    public GameLauncher() {
        this.workingDirectory = new File(System.getProperty("user.dir"), "settings");

        this.armors = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
        this.players = new ArrayList<>();
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

    private void setupFrame(JFrame frame) {
        frame.setSize(540, 650);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
//        ((GameWindow)frame).mainPanel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g1) {
//                super.paintComponent(g1);
//
//                Graphics2D g = (Graphics2D) g1;
//                g.setRenderingHint(
//                        RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
//
//                g.setRenderingHint(
//                        RenderingHints.KEY_TEXT_ANTIALIASING,
//                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//                g.drawImage(getWeapons().get(0).getImage(), 100, 100, 100, 100, null);
//            }
//        };
    }

    public File getWorkingDirectory() {
        return this.workingDirectory;
    }

    public List<Armor> getArmors() {
        return armors;
    }

    public List<Potion> getPotions() {
        return potions;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
