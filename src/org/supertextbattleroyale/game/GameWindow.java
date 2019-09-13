package org.supertextbattleroyale.game;

import org.supertextbattleroyale.Main;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;

public class GameWindow extends JFrame {
    public JPanel mainPanel;
    private JPanel gamePanel;
    private JTextPane textPane1;
    private GameLauncher launcher;

    public GameWindow(GameLauncher launcher) {
        this.add(this.mainPanel);
        this.launcher = launcher;
        this.setupRepainting();
    }

    private Graphics2D setupGraphics(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return graphics2D;
    }

    private void createUIComponents() {
        //Custom setup of the game panel so that it draws correctly and where we want it to draw
        this.gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                drawGameScreen(setupGraphics(g));
            }
        };

//        this.gamePanel.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setBounds(512, 512, 512, 512);
    }

    private void setupRepainting() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new PaintTimerTask(this), 0, 1000);
    }

    private void drawGameScreen(Graphics2D g) {
        //TODO: HEAVY TESTING: DO NOT MESS

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.gamePanel.getWidth(), this.gamePanel.getHeight());
        this.launcher.getLoadedMaps().get(0).draw(g);

        Color ground = new Color(0, 200, 50, 49);
        Color wall = new Color(33, 33, 33, 150);

        Tile[][] tiles = this.launcher.getLoadedMaps().get(0).getMatrixMap();

        int w1 = Math.floorDiv(this.gamePanel.getWidth(), tiles.length);
        int w2 = Math.floorDiv(this.gamePanel.getHeight(), tiles[0].length);

        int tileWidth = Math.min(w1, w2);

        int xDist = (this.gamePanel.getWidth() - tileWidth * tiles.length) / 2;
        int yDist = (this.gamePanel.getHeight() - tileWidth * tiles[0].length) / 2;

        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];

//            for (int j = 0; j < row.length; j++) {
//                g.setColor(tiles[i][j] instanceof Ground ? ground : wall);
//                g.fillRect(xDist + i * tileWidth, yDist + j * tileWidth, tileWidth, tileWidth);
//                g.setColor(Color.BLACK);
//                g.drawRect(xDist + i * tileWidth, yDist + j * tileWidth, tileWidth, tileWidth);
//            }
        }

        this.launcher.getGameInstance().drawComponents(g);
    }

    public JPanel getGamePanel() {
        return this.gamePanel;
    }
}
