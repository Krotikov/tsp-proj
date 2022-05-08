package Project.modules.utils;

import java.util.Random;

public class CustomRandom {
    private static final Random rd = new Random();

    public static int inRange(int min, int max) {
        return rd.nextInt(max - min) + min;
    }

    public static double inRange(double min, double max) {
        Random rd = new Random();
        return rd.nextDouble(max - min) + min;
    }

    @SafeVarargs
    public static <T> T from(T... items) {
        return items[rd.nextInt(items.length)];
    }
}
