package org.supertextbattleroyale.game;

import org.javatuples.Triplet;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends JFrame {
    private final int FPS = 60;
    private final float TICKS_PER_SECOND = 1f;

    public JPanel mainPanel;
    private JPanel gamePanel;

    private final Timer timer;
    private TimerTask currentTask;

    private int currentTick;

    private float xTranslate, yTranslate;
    private int xZoom, yZoom;
    private float zoom;

    public GameWindow() {
        this.add(this.mainPanel);
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

        new Thread(new onTickTimerTask(this)).start();
//        this.timer.scheduleAtFixedRate(this.currentTask = new onTickTimerTask(this), 0, 1);
    }

    private void onTick(Graphics2D g) {
        if (GameLauncher.getGameInstance() == null) return;

        g.translate(this.xZoom, this.yZoom);
        g.scale(this.zoom, this.zoom);
        g.translate(-this.xZoom, -this.yZoom);

        if (this.currentTick % (FPS / TICKS_PER_SECOND) == 0) {
            GameLauncher.getGameInstance().onTick();
            this.currentTick = 0;
        }

        GameLauncher.getGameInstance().drawComponents(g);

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
}
