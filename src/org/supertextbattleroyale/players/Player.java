package org.supertextbattleroyale.players;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.utils.JsonUtils;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class Player {

    private String name;
    private Icon icon;

    public Player(File directory) throws JsonLoadFailException {
        this.setupFromJson(new File(directory, "config.json"));
        this.icon = new ImageIcon(new File(directory, "icon.png").getAbsolutePath());
    }

    private void setupFromJson(File config) throws JsonLoadFailException {
        Optional<JsonElement> jsonElement = JsonUtils.getJsonElementFromFile(config);

        if (jsonElement.isEmpty()) throw new JsonLoadFailException();

        JsonObject jsonObject = jsonElement.get().getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, entry.getValue().getAsString());
            } catch (NoSuchFieldException | IllegalAccessException exx) {
                exx.printStackTrace();

                throw new JsonLoadFailException();
            }
        }
    }

    public String getName() {
        return this.name;
    }
}
