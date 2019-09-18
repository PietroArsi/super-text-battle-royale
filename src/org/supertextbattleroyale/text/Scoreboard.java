package org.supertextbattleroyale.text;

import org.supertextbattleroyale.interfaces.Drawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scoreboard implements Drawable {

    private String currentAction;
    private List<String> actions;

    public Scoreboard() {
        this.actions = new ArrayList<>();
        this.currentAction = "";
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        //adds it to the list of old messages
        if(!this.currentAction.isEmpty()) {
            this.actions.add(this.currentAction);
        }
        //changes it
        this.currentAction = currentAction;
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
