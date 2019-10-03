package org.supertextbattleroyale.game_;

import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.utils.TimerUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {

    private int PWIDTH = 512;
    private int PHEIGHT = 512;

    private Thread animator;

    private volatile boolean running;
    private volatile boolean gameOver;
    private volatile boolean isPaused;

    private volatile int fpsPeriod;
    private volatile int upsPeriod;

    public GamePanel() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
    }

    @Override
    public void addNotify() {
        super.addNotify();

        this.startGame();
    }

    private void startGame() {
        if (this.animator == null || !this.running) {
            this.animator = new Thread(this);
            this.animator.start();

            this.setMaxFPS(60);
            this.setUPS(2);

            this.running = true;
        }
    }

    public void stopGame() {
        this.running = false;
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    TimerUtils timer = new TimerUtils();

    @Override
    public void run() {
        long before, timeDiff, sleepTime;

        before = System.currentTimeMillis();

        while (running) {
            if(timer.hasReach(upsPeriod)) {
                this.updateGame();
                timer.reset();
            }
            this.updateRender();
            this.repaint();

            timeDiff = System.currentTimeMillis() - before;
            sleepTime = fpsPeriod - timeDiff;

            if (sleepTime <= 0) {
                sleepTime = 5;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            before = System.currentTimeMillis();
        }

        System.exit(0);
    }

    public void setMaxFPS(int FPS) {
        this.fpsPeriod = 1000 / FPS;
    }

    public void setUPS(int UPS) {
        this.upsPeriod = 1000 / UPS;
    }

    private void updateGame() {
        if (this.isPaused) return;

        if (GameLauncher.getGameInstance() == null) return;

        GameLauncher.getGameInstance().onTick();
    }

    private Graphics2D dbg;
    private Image dbImage;

    private void updateRender() {
        if (this.isPaused) return;

        if (dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);

            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            }

            dbg = setupGraphics(dbImage.getGraphics());
        }

        if (GameLauncher.getGameInstance() == null) return;

//        dbg.translate(this.xZoom, this.yZoom);
//        dbg.scale(this.zoom, this.zoom);
//        dbg.translate(-this.xZoom, -this.yZoom);

        GameLauncher.getGameInstance().drawComponents(dbg);
    }

    public static Graphics2D setupGraphics(Graphics g) {
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

    public void setupDimension(int width, int height) {
        this.PWIDTH = width;
        this.PHEIGHT = height;

        dbImage = createImage(PWIDTH, PHEIGHT);

        if (dbImage == null) {
            System.out.println("dbImage is null");
            return;
        }

        dbg = setupGraphics(dbImage.getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dbImage != null)
            g.drawImage(dbImage, 0, 0, null);
    }
}
