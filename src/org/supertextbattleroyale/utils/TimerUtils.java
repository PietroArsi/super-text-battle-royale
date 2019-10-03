package org.supertextbattleroyale.utils;

import java.util.concurrent.TimeUnit;

public class TimerUtils {
    private long last;

    private double convert(double d) {
        return 1000 / d;
    }

    public final boolean hasReach(final double d) {
        return this.hasReach(d, false);
    }

    public final boolean hasReach(final double d, final boolean convert) {
        return sleep(convert ? convert(d) : d, TimeUnit.MILLISECONDS);
    }

    private boolean sleep(final double time, TimeUnit timeUnit) {
        final long convert = timeUnit.convert(System.nanoTime() - last, TimeUnit.NANOSECONDS);
        return convert >= time;
    }

    public final long getTimePassed() {
        return (System.nanoTime() - last) / 1000000;
    }

    public final void reset() {
        last = System.nanoTime();
    }

    public long getCurrentMS() {
        return System.nanoTime();
    }

    public long getLastMS() {
        return last;
    }
}