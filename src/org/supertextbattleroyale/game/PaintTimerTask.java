package org.supertextbattleroyale.game;

import java.util.TimerTask;

public class PaintTimerTask extends TimerTask {

    private GameWindow window;

    public PaintTimerTask(GameWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        this.window.getGamePanel().repaint();
    }
}