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

    public Collectible(File settingsFolder) throws JsonLoadFailException {
        this.setupFromJson(new File(settingsFolder, "config.json"));
        try {
            this.icon = ImageIO.read(new FileInputStream(new File(settingsFolder, "icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonLoadFailException();
        }
    }

    private void setupFromJson(File config) throws JsonLoadFailException {
        Optional<JsonElement> jsonElement = JsonUtils.getJsonElementFromFile(config);

        if (jsonElement.isEmpty()) throw new JsonLoadFailException();

        JsonObject jsonObject = jsonElement.get().getAsJsonObject();
        Gson gson = new Gson();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, gson.fromJson(entry.getValue(), field.getType()));
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                try {
                    Field field = this.getClass().getSuperclass().getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(this, gson.fromJson(entry.getValue(), field.getType()));
                } catch (NoSuchFieldException | IllegalAccessException exx) {
                    exx.printStackTrace();

                    throw new JsonLoadFailException();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return this.icon;
    }
}
