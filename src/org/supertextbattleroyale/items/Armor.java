package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;

import java.io.File;

public class Armor extends Collectible {
    public Armor(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
    }
}
