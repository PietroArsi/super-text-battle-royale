package org.supertextbattleroyale.game;

import org.javatuples.Quintet;

import java.util.TimerTask;

public class onTickTimerTask extends TimerTask {

    private GameWindow window;

    public onTickTimerTask(GameWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        this.window.getGamePanel().repaint();
        this.window.getScoreboardPanel().repaint();
    }
}