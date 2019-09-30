package org.supertextbattleroyale.game;

import org.javatuples.Triplet;
import org.supertextbattleroyale.game_.GamePanel;

import javax.swing.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements WindowListener, WindowFocusListener, WindowStateListener {
    private JPanel mainPanel;
    private JPanel gamePanel;

    private int xZoom, yZoom;
    private float zoom;

    GameWindow() {
        this.add(this.mainPanel);
        this.setupZoom(0, 0, 1);

        this.addWindowListener(this);
        this.addWindowFocusListener(this);
        this.addWindowListener(this);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                getGamePanel().setupDimension(getWidth(), getHeight());
                System.out.println(getWidth() + " " + getHeight());
            }
        });
    }

    private void createUIComponents() {
        //Custom setup of the game panel so that it draws correctly and where we want it to draw
        this.gamePanel = new GamePanel();

        this.setBounds(512, 512, 512, 512);
    }

    public Triplet<Integer, Integer, Float> getZoomStatus() {
        return new Triplet<>(this.xZoom, this.yZoom, this.zoom);
    }

    public void setupZoom(int xZ, int yZ, float zoom) {
        this.zoom = zoom;
        //TODO:
        this.xZoom = xZ;
        this.yZoom = yZ;
    }

    public GamePanel getGamePanel() {
        return (GamePanel) this.gamePanel;
    }

    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        getGamePanel().resume();
    }

    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        getGamePanel().pause();
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        getGamePanel().stopGame();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
        getGamePanel().stopGame();
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        getGamePanel().pause();
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        getGamePanel().resume();
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
        getGamePanel().resume();
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
        getGamePanel().pause();
    }

    @Override
    public void windowStateChanged(WindowEvent windowEvent) {

    }
}
