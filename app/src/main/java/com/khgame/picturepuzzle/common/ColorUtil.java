package com.khgame.picturepuzzle.common;

/**
 * Created by zkang on 2017/4/9.
 */

public class ColorUtil {

    public static int darker(int color) {
        int R = 0xFF0000;
        int G = 0x00FF00;
        int B = 0x0000FF;

        int r = (color & R) >> 16;
        int g = (color & G) >> 8;
        int b = (color & B) >> 4;

        r = decrease(r);
        g = decrease(g);
        b = decrease(b);

        int newColor = r << 16 + g << 8 + b << 4;
        return newColor;
    }

    /**
     * decrease the value by 30%
     */
    private static int decrease(int value) {
        return (int)(value * 0.7);
    }

}
