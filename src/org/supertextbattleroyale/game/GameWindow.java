package org.supertextbattleroyale.game;

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
    private GameLauncher launcher;

    private final Timer timer;
    private TimerTask currentTask;

    private int currentTick;
    private float wheel, wheelSpeed, wheelAcc;

    private float xTranslate, yTranslate, xm, ym;

    private final int FPS = 60;

    public GameWindow(GameLauncher launcher) {
        this.add(this.mainPanel);
        this.launcher = launcher;
        this.timer = new Timer();

        this.setupTimer(500);
        this.currentTick = 0;
        this.wheel = 1;
        this.xTranslate = 0;
        this.yTranslate = 0;
        this.wheelSpeed = 1;

        this.addMouseWheelListener(mouseWheelEvent -> {
            wheelSpeed += -0.1f * mouseWheelEvent.getWheelRotation();
            xm = mouseWheelEvent.getX();
            ym = mouseWheelEvent.getY();
        });
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

        graphics2D.transform(new AffineTransform(wheel, 0, 0, wheel, xTranslate, yTranslate));
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

        g.translate(getWidth()/2, getHeight()/2);
        g.scale(this.wheelSpeed, this.wheelSpeed);
        g.translate(-getWidth()/2, -getHeight()/2);

        if (this.currentTick % 60 == 0) {
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
