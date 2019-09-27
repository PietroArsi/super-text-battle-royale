package org.supertextbattleroyale.game;

public class onTickTimerTask implements Runnable {

    private GameWindow window;

    public onTickTimerTask(GameWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        while (true) {
            this.window.getGamePanel().repaint();
        }
    }
}