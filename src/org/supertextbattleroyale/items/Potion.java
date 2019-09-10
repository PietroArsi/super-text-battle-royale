package org.supertextbattleroyale.items;

import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.items.base.Collectible;

import java.io.File;

public class Potion extends Collectible {
    public Potion(File settingsFolder) throws JsonLoadFailException {
        super(settingsFolder);
    }
}