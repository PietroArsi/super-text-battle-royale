package org.supertextbattleroyale.game;

import org.javatuples.Triplet;
import org.supertextbattleroyale.maps.tiles.Ground;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends JFrame {
    public JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel scoreboardPanel;
    private GameLauncher launcher;

    private final Timer timer;
    private TimerTask currentTask;

    private int currentTick;

    private float xTranslate, yTranslate;
    private int xZoom, yZoom;
    private float zoom;

    private final int FPS = 60;

    public GameWindow(GameLauncher launcher) {
        this.add(this.mainPanel);
        this.launcher = launcher;
        this.timer = new Timer();

        this.setupTimer(500);
        this.currentTick = 0;
        this.xTranslate = 0;
        this.yTranslate = 0;
        this.setupZoom(0, 0, 1);
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

        graphics2D.transform(new AffineTransform(1, 0, 0, 1, xTranslate, yTranslate));
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

        int period = 1000 / FPS;
        this.timer.scheduleAtFixedRate(this.currentTask = new onTickTimerTask(this), 0, period);
    }

    private void onTick(Graphics2D g) {
        if (this.launcher.getGameInstance() == null) return;

        g.translate(this.xZoom, this.yZoom);
        g.scale(this.zoom, this.zoom);
        g.translate(-this.xZoom, -this.yZoom);

        if (this.currentTick % 60 == 0) {
            this.launcher.getGameInstance().onTick();
            this.currentTick = 0;
        }

        this.launcher.getGameInstance().drawComponents(g);


        this.currentTick++;
    }

    public Triplet<Integer, Integer, Float> getZoomStatus() {
        return new Triplet<>(this.xZoom, this.yZoom, this.zoom);
    }

    public void setupZoom(int xZ, int yZ, float zoom) {
        this.zoom = zoom;
        this.xZoom = xZ;
        this.yZoom = yZ;
    }

    public JPanel getGamePanel() {
        return this.gamePanel;
    }

    public JPanel getScoreboardPanel() {
        return this.scoreboardPanel;
    }
}
