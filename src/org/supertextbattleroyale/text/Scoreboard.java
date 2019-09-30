package org.supertextbattleroyale.text;

import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.game.GameWindow;
import org.supertextbattleroyale.interfaces.Drawable;
import org.supertextbattleroyale.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scoreboard implements Drawable {

    private String currentAction;
    private List<String> actions;

    private int width, height;

    public Scoreboard(int width, int height) {
        this.actions = new ArrayList<>();
        this.currentAction = "";

        this.setupDimensions(width, height);
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        //adds it to the list of old messages
        if (!this.currentAction.isEmpty()) {
            this.actions.add(this.currentAction);
        }
        //changes it
        this.currentAction = currentAction;
    }


    public void setupDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g) {
        JPanel gamePanel = GameLauncher.getMainFrame().getGamePanel();
        this.setupDimensions(gamePanel.getWidth(), gamePanel.getHeight());

        int xDist = gamePanel.getWidth() / 3 * 2 + gamePanel.getWidth() / 6;
        int yDist = 100;

        g.setFont(new Font("TSCu_Comic", Font.BOLD, gamePanel.getWidth() / 30));

        List<StringUtils.ColoredString> l1 = StringUtils.getLinesFromWidth(g, "^[1446a0]SUPER ^[0b4f6c]TEXT ^[db3069]BATTLE ^[f5d547]ROYALE", gamePanel.getWidth() / 3);
        Point p = StringUtils.drawCenteredColoredStringList(g, l1, xDist + gamePanel.getX(), yDist + gamePanel.getY());

        g.setFont(new Font("C059", Font.BOLD, gamePanel.getWidth() / 45));
        List<StringUtils.ColoredString> l2 = StringUtils.getLinesFromWidth(g, "^[ebebd3](now with graphics!)", gamePanel.getWidth() / 3);
        StringUtils.drawCenteredColoredStringList(g, l2, xDist + gamePanel.getX(), p.y);
    }
}
