package org.supertextbattleroyale.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUtils {

    /**
     * Returns a String with all lines the lines concatenated into
     *
     * @param file to read from
     * @return the String or empty if file doesn't exist
     */
    public static Optional<String> getAllLinesFromFile(File file) {
        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return Optional.of(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Gets a List of Strings
     *
     * @param file to read from
     * @return List of String where every String is a file line
     */
    public static List<String> getLinesFromFile(File file) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
