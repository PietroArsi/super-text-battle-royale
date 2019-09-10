package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;

import java.io.File;

public class Weapon extends Collectible {
    public Weapon(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
    }
}
