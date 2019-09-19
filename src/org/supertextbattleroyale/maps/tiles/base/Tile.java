package org.supertextbattleroyale.maps.tiles.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.utils.JsonUtils;

import java.io.File;
import java.util.Optional;

public abstract class Tile {

    private boolean isWalkable;
    private boolean isBulletProof; //True if bullet can't pass through tile

    public abstract boolean isTileWalkable();
    public abstract boolean isTileBulletproof();
    public abstract boolean isTileTransparent();

    private char symbol;

    public Tile(File config) throws JsonLoadFailException {
        this.loadSymbol(config);
    }

    /**
     * Gets the corresponding tile symbol from the tiles.json configuration file
     * Uses the current class name (not from Tile class since it's abstract)
     *
     * @param config tiles.json
     * @throws JsonLoadFailException if the current class symbol is not present
     */
    private void loadSymbol(File config) throws JsonLoadFailException {
        Optional<JsonElement> jsonElement = JsonUtils.getJsonElementFromFile(config);

        if(jsonElement.isEmpty()) throw new JsonLoadFailException();

        JsonElement entry = jsonElement.get().getAsJsonObject().get(this.getClass().getSimpleName());

        if(entry == null) throw new JsonLoadFailException();

        this.symbol = entry.getAsCharacter();
    }

    public char getSymbol() {
        return this.symbol;
    }
}
