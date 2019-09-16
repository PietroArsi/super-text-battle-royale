package org.supertextbattleroyale.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorUtils {

    public static Color makeColorGradient(float frequency1, float frequency2, float frequency3,
                                          int phase1, int phase2, int phase3,
                                          int where, int center, int width) {
        int red = (int) (Math.sin(frequency1 * where + phase1) * width + center);
        int green = (int) (Math.sin(frequency2 * where + phase2) * width + center);
        int blue = (int) (Math.sin(frequency3 * where + phase3) * width + center);
        return new Color(red, green, blue, 255);
    }

    /**
     * Dyes given BufferedImage with given Color
     *
     * @param image The image to be dyed
     * @param color The color to dye with
     * @return The dyed BufferdImage
     */
    public static BufferedImage tintImage(BufferedImage image, Color color) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        Color lowAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 125);
        g.setColor(lowAlpha);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return dyed;
    }
}
