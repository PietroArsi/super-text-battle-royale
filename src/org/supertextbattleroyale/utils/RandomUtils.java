package org.supertextbattleroyale.utils;

import java.util.Random;

public class RandomUtils {
    public static Random generator = new Random();

    /**
     *
     * @param min lowest number that can be generated (inclusive)
     * @param max highest number that can be generated (inclusive)
     * @return Generate a random integer in range [min, max]
     */
    public static int randomIntRange(int min, int max) {
        return generator.nextInt(max - min + 1) + min;
    }
}
