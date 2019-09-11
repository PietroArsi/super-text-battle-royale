package org.supertextbattleroyale.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUtils {

    public static Optional<String> getAllLinesFromFile(File file) {
        if (!file.exists()) {
            System.out.println(file.getAbsolutePath());
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
