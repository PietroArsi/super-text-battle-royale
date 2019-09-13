package org.supertextbattleroyale.items.base;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.supertextbattleroyale.exceptions.JsonLoadFailException;
import org.supertextbattleroyale.utils.JsonUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.util.Map;
import java.util.Optional;

public class Collectible {

    private String name;
    private BufferedImage icon;

    protected File settingsFolder;

    public Collectible(File settingsFolder) throws JsonLoadFailException {
        this.settingsFolder = settingsFolder;

        this.setupFromJson(new File(settingsFolder, "config.json"));
        this.setupIcon(settingsFolder);
    }

    /**
     * 
     * @param config
     * @throws JsonLoadFailException
     */
    private void setupFromJson(File config) throws JsonLoadFailException {
        Optional<JsonElement> jsonElement = JsonUtils.getJsonElementFromFile(config);

        if (jsonElement.isEmpty()) throw new JsonLoadFailException();

        JsonObject jsonObject = jsonElement.get().getAsJsonObject();
        Gson gson = new Gson();

        //TODO: fix if something is not found
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            try {
                for(Class c = this.getClass(); c != Object.class; c = c.getSuperclass()) {
                    Field field = c.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(this, gson.fromJson(entry.getValue(), field.getType()));
                }
            } catch (NoSuchFieldException | IllegalAccessException ex) {
//                throw new JsonLoadFailException();
            }
        }
    }

    private void setupIcon(File settingsFolder) throws JsonLoadFailException {
        try {
            this.icon = ImageIO.read(new FileInputStream(new File(settingsFolder, "icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonLoadFailException();
        }
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return this.icon;
    }
}
