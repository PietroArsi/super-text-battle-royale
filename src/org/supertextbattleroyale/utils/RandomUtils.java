package org.supertextbattleroyale.utils;

import java.util.Random;

public class RandomUtils {
    private final static Random generator = new Random();

    /**
     * Generates a random integer in range [min, max]
     *
     * @param min lowest number that can be generated (inclusive)
     * @param max highest number that can be generated (inclusive)
     * @return the generated value
     */
    public static int randomIntRange(int min, int max) {
        return generator.nextInt(max - min + 1) + min;
    }
}
