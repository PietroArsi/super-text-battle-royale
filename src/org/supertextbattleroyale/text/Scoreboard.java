package org.supertextbattleroyale.text;

import org.supertextbattleroyale.game.GameLauncher;
import org.supertextbattleroyale.interfaces.Drawable;

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

    private List<String> getLinesFromWidth(Graphics2D g, String s, int width) {
        List<String> list = new ArrayList<>();

        String[] split = s.split(" ");
        int counter = 0;
        StringBuilder sb = new StringBuilder("");
        while (counter != split.length) {
            String current = split[counter];
            int wordWidth = (int) g.getFontMetrics().getStringBounds(current, g).getWidth();
            sb.append(current).append(" ");
            int stringWidth = (int) g.getFontMetrics().getStringBounds(sb.toString().trim(), g).getWidth();

            //CASE String is too long for one line
            if (wordWidth >= width) {
                sb.delete(sb.length() - 1 - current.length(), sb.length());
                int counter2 = 0;
                while (counter2 != current.length()) {
                    sb.append(current.charAt(counter2));

                    if (g.getFontMetrics().getStringBounds(sb.toString(), g).getWidth() >= width) {
                        sb.delete(sb.length() - 1, sb.length());
                        list.add(sb.toString().trim());
                        sb.delete(0, sb.length());
                    } else {
                        counter2++;
                    }
                }
                sb.append(" ");
                counter++;
            } else if (stringWidth > width) {//CASE The current appended word is too much for the current width
                sb.delete(sb.length() - 1 - split[counter].length(), sb.length());
                list.add(sb.toString().trim());
                sb.delete(0, sb.length());
            } else {//append went okay
                counter++;
            }
        }

        if (sb.length() > 0) {
            list.add(sb.toString());
        }

        return list;
    }

    public void setupDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g) {
        JPanel gamePanel = GameLauncher.getMainFrame().getGamePanel();
        this.setupDimensions(gamePanel.getWidth(), gamePanel.getHeight());

        int xDist = gamePanel.getWidth() / 3 * 2 + 5;
        int yDist = 100;

        g.setColor(Color.white);
        g.setFont(new Font("Roboto", Font.BOLD, gamePanel.getWidth() / 33));

        List<String> l = getLinesFromWidth(g, "Dario assorda Giammi con la Trap quella bella", gamePanel.getWidth() / 3);
        for (int i = 0; i < l.size(); i++) {
            g.drawString(l.get(i), xDist + gamePanel.getX(), yDist + gamePanel.getY() + i * (g.getFont().getSize() + 2));
        }
    }
}
