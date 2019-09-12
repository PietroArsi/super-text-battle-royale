package org.supertextbattleroyale.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class GameWindow extends JFrame {
    public JPanel mainPanel;
    private JTextArea textArea1;
    private JPanel gamePanel;
    private JTextArea SUPERTEXTBATTLEROYALETextArea;
    private GameLauncher launcher;

    public GameWindow(GameLauncher launcher) {
        this.add(this.mainPanel);
        this.launcher = launcher;
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

        this.gamePanel.setBounds(0, 0, 512, 512);
    }

    private void drawGameScreen(Graphics2D g) {
        g.drawImage(launcher.getWeapons().get(0).getImage(), 0, 0, 100, 100, null);
    }
}
