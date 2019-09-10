package org.supertextbattleroyale.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.Optional;

public class JsonUtils {

    private static final JsonParser parser = new JsonParser();

    public static Optional<JsonElement> getJsonElementFromFile(File file) {
        Optional<String> optString = FileUtils.getAllLinesFromFile(file);

        if(optString.isEmpty()) return Optional.empty();

        return Optional.of(parser.parse(optString.get()));
    }
}
