package org.supertextbattleroyale.utils;

import org.javatuples.Triplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringUtils {

    public static class ColoredString {
        private List<Color> colors = new ArrayList<>();
        private String string;

        private List<ColoredString> getWords() {
            List<ColoredString> words = new ArrayList<>();

            StringBuilder current = new StringBuilder();
            ColoredString word = new ColoredString();

            for (int i = 0; i < string.toCharArray().length; i++) {
                char c = string.charAt(i);

                if (c == ' ') {
                    word.string = current.toString();
                    current.delete(0, current.length());
                    words.add(word);
                    word = new ColoredString();
                    continue;
                }

                current.append(c);
                word.colors.add(colors.get(i));
            }

            return words;
        }

        @Override
        public String toString() {
            return this.string;
        }

        public List<Color> getColors() {
            return this.colors;
        }
    }

    public static int getCharWidth(char c, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(c + "", g).getWidth();
    }

    public static int getStringWidth(String s, Graphics2D g) {
        return (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
    }

    public static void addCharsToList(List<Triplet<Character, Integer, Color>> list, Graphics2D g, String s, Color color) {
        int currentWidth = list.get(list.size() - 1).getValue1();
        for (char c : s.toCharArray()) {
            list.add(new Triplet<>(c, currentWidth, new Color(RandomUtils.randomIntRange(0, 255), 100, 100)));
            currentWidth += g.getFontMetrics().getStringBounds(c + "", g).getWidth();
        }
    }

    private static ColoredString getColoredStringFromSplit(String[] split) {
        Color currentColor = Color.black;

        ColoredString cs = new ColoredString();
        StringBuilder sb = new StringBuilder();

        for (String string : split) {
            if (isColorCode(string)) {
                currentColor = getColorFromCode(string);
                continue;
            }

            for (char c : string.toCharArray()) {
                sb.append(c);
                cs.colors.add(currentColor);
            }
        }

        cs.string = sb.toString();

        return cs;
    }

    /**
     * Given a string splits it into lines and colors it accordingly to color codes found.
     * Those are defined as ^[hexcode]
     *
     * @param g
     * @param s
     * @param width
     * @return
     */
    public static List<ColoredString> getLinesFromWidth(Graphics2D g, String s, int width) {
        String[] splitColorCodes = s.split("(?=\\^\\[(\\w){6}])|(?<=\\^\\[(\\w){6}])");

        ColoredString cs = getColoredStringFromSplit(splitColorCodes);
        List<ColoredString> list = new ArrayList<>();

        String[] split = cs.string.split(" ");
        int counter = 0;
        int readChars = 0;
        List<Color> currentColors = new CopyOnWriteArrayList<>();

        StringBuilder sb = new StringBuilder("");
        while (counter != split.length) {
            String current = split[counter];
            int wordWidth = (int) g.getFontMetrics().getStringBounds(current, g).getWidth();
            sb.append(current).append(" ");
            int stringWidth = (int) g.getFontMetrics().getStringBounds(sb.toString().trim(), g).getWidth();

            //CASE String is too long for one line
            if (wordWidth >= width) {
                sb.delete(sb.length() - 1 - current.length(), sb.length());
                int counter2 = 0;
                while (counter2 != current.length()) {
                    sb.append(current.charAt(counter2));

                    if (g.getFontMetrics().getStringBounds(sb.toString(), g).getWidth() >= width) {
                        sb.delete(sb.length() - 1, sb.length());

                        //COLORED STRING MAKING
                        ColoredString newCs = new ColoredString();
                        newCs.string = sb.toString().trim();
                        newCs.colors.addAll(currentColors);
                        currentColors.clear();

                        list.add(newCs);
                        sb.delete(0, sb.length());
                    } else {
                        currentColors.add(cs.colors.get(readChars));
                        counter2++;
                        readChars++;
                    }
                }
                sb.append(" ");
                counter++;
            } else {
                for (int i = readChars; i < readChars + current.length() + 1; i++) {
                    if(i < cs.colors.size()) currentColors.add(cs.colors.get(i));
                }
                readChars += current.length();

                if (stringWidth > width) {//CASE The current appended word is too much for the current width
                    sb.delete(sb.length() - 1 - split[counter].length(), sb.length());

                    for (int k = 0; k < split[counter].length(); k++) {
                        currentColors.remove(currentColors.size() - 1);
                        readChars--;
                    }

                    //COLORED STRING MAKING
                    ColoredString newCs = new ColoredString();
                    newCs.string = sb.toString().trim();
                    newCs.colors.addAll(currentColors);
                    currentColors.clear();

                    list.add(newCs);
                    sb.delete(0, sb.length());
                } else {//append went okay
                    counter++;
                    readChars++;
                }
            }
        }

        if (sb.length() > 0) {
            //COLORED STRING MAKING
            ColoredString newCs = new ColoredString();
            newCs.string = sb.toString().trim();
            newCs.colors.addAll(currentColors);
            currentColors.clear();

            list.add(newCs);
        }

        return list;
    }

    public static Point drawCenteredColoredStringList(Graphics2D g, List<ColoredString> l, int x, int y) {
        int maxLength = 0;

        for (int i = 0; i < l.size(); i++) {
            int length = StringUtils.drawCenteredColoredString(g, l.get(i), x, y + i * (g.getFont().getSize() + 2));
            if(length > maxLength) {
                maxLength = length;
            }
        }

        return new Point(maxLength + 2, y + l.size() * (g.getFont().getSize()));
    }

    public static Point drawColoredStringList(Graphics2D g, List<ColoredString> l, int x, int y) {
        int maxLength = 0;

        for (int i = 0; i < l.size(); i++) {
            int length = StringUtils.drawColoredString(g, l.get(i), x, y + i * (g.getFont().getSize() + 2));
            if(length > maxLength) {
                maxLength = length;
            }
        }

        return new Point(maxLength + 2, y + l.size() * (g.getFont().getSize() + 2));
    }

    public static int drawCenteredColoredString(Graphics2D g, ColoredString cs, int x, int y) {
        int sWidth = 0;
        int stringWidth = getStringWidth(getRawString(cs.toString()), g);

        x = x - stringWidth/2;

        for (int j = 0; j < cs.toString().toCharArray().length; j++) {
            g.setColor(cs.getColors().get(j));
            g.drawString(cs.toString().charAt(j) + "", x + sWidth, y);

            sWidth += StringUtils.getCharWidth(cs.toString().charAt(j), g);
        }
        return sWidth;
    }

    public static int drawColoredString(Graphics2D g, ColoredString cs, int x, int y) {
        int sWidth = 0;
        for (int j = 0; j < cs.toString().toCharArray().length; j++) {
            g.setColor(cs.getColors().get(j));
            g.drawString(cs.toString().charAt(j) + "", x + sWidth, y);

            sWidth += StringUtils.getCharWidth(cs.toString().charAt(j), g);
        }
        return sWidth;
    }

    private static boolean isColorCode(String s) {
        return s.matches("\\^\\[(\\w){6}]");
    }

    private static Color getColorFromCode(String code) {
        String hexFormat = code.replace("^[", "").replace("]", "");
        return new Color(Integer.parseInt(hexFormat, 16));
    }

    private static String getRawString(String formattedString) {
        return formattedString.replaceAll("\\^\\[(\\w){6}]", "");
    }
}
