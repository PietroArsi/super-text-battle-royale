package org.supertextbattleroyale.maps.tiles;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;

import java.io.File;

public class Chest extends LowObstacle {
    private boolean empty;
    private Collectible item;
    //TODO: Set two Images, one for the opened chest, one for the closed chest

    public Chest(File config) throws JsonLoadFailException {
        super(config);

        this.empty = false;
    }

    public void setItem(Collectible item) {
        this.item = item;
    }

    public Collectible getItem() {
        return this.item;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public Collectible loot() {
        empty = true;
        return this.item;
    }

}
