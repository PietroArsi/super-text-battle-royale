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

    /**
     * Simulates a single Bernoulli Trial
     *
     * @param p Probability of success (must be between 0f and 1f)
     * @return 1 or 0
     */
    public static int bernoulli(float p) {
        return generator.nextDouble() > 1 - p ? 1 : 0;
    }

    /**
     * Flips a PseudoBitcoin (NOW WITH BLOCKCHAIN 3.5)
     *
     * @return 1 with probability 0.5, 0 with probabilty 0.5, 7.9f with probability 0
     */
    public static int flipACoin() {
        return bernoulli(0.5f);
    }

    /**
     * Simulates a Binomial distribution
     *
     * @param n Number of Bernoulli trials
     * @param p Success probability of each Bernoulli trial
     * @return A number between 0 and n with a Binomial distribution (The sum of n independent Bernoulli trials)
     */
    public static int binomial(int n, float p) {
        int success = 0;
        for (int i = 0; i < n; i++) success += bernoulli(p);
        return success;
    }

    /**
     * Like a gaussian random variable but with integer
     *
     * @param min lowest number that can be generated (inclusive)
     * @param max highest number that can be generated (inclusive)
     * @return a number between min and max with a Binomial distribution
     */
    public static int translatedBinomial(int min, int max) {
        return binomial(max - min, 0.5f) + min;
    }

    /**
     * Simulate a gaussian random variable
     *
     * @param mean the mean of the distribution
     * @param var  the variance of the distribution
     * @return
     */
    public static float gaussian(float mean, float var) {
        return mean + (float) generator.nextGaussian() * var;
    }

    public static float exponential(float lambda) {
        return -(float) Math.log(1 - (1 - Math.exp(-lambda)) * generator.nextDouble()) / lambda;
    }
}
