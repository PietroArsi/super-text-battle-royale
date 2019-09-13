package org.supertextbattleroyale.game;

import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends JFrame {
    public JPanel mainPanel;
    private JPanel gamePanel;
    private JTextPane textPane1;
    private GameLauncher launcher;

    private final Timer timer;
    private TimerTask currentTask;

    private int currentTick;

    public GameWindow(GameLauncher launcher) {
        this.add(this.mainPanel);
        this.launcher = launcher;
        this.timer = new Timer();

        this.setupTimer(500);
        this.currentTick = 0;
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

                onTick(setupGraphics(g));
            }
        };

        this.setBounds(512, 512, 512, 512);
    }

    private void setupTimer(int ms) {
        if (this.currentTask != null) {
            this.currentTask.cancel();
        }
        this.timer.scheduleAtFixedRate(this.currentTask = new onTickTimerTask(this), 0, 250);
    }

    private void onTick(Graphics2D g) {
        if (this.launcher.getGameInstance() == null) return;

        if (this.currentTick % 10 == 0) {
            this.launcher.getGameInstance().onTick();
            this.currentTick = 0;
        }

        this.launcher.getGameInstance().drawComponents(g);

        this.currentTick++;
    }

    public JPanel getGamePanel() {
        return this.gamePanel;
    }
}
