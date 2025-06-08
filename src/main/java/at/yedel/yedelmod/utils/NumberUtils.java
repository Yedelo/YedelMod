package at.yedel.yedelmod.utils;



public class NumberUtils {
    public static double randomRange(int min, int max) {
        return Math.random() * (max - min) + min;
    }

    // https://stackoverflow.com/a/22186845
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
