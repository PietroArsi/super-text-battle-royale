package org.supertextbattleroyale.game;

import java.util.TimerTask;

public class onTickTimerTask extends TimerTask {

    private GameWindow window;

    public onTickTimerTask(GameWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        while (true) {

//            if (this.currentTick % (FPS / TICKS_PER_SECOND) == 0) {
                if(GameLauncher.getGameInstance() != null) {
                    GameLauncher.getGameInstance().onTick();
                }
//                this.currentTick = 0;
//            }

            this.window.getGamePanel().repaint();
        }
    }
}