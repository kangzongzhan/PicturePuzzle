package com.khgame.picturepuzzle.common;

/**
 * Created by zkang on 2017/4/9.
 */

public class ColorUtil {

    public static int darker(int color) {
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0x00FF00) >> 8;
        int b = (color & 0X0000FF) >> 4;

        r = decrease(r);
        g = decrease(g);
        b = decrease(b);

        return r << 16 + g << 8 + b << 4;
    }

    /**
     * decrease the value by 30%
     */
    private static int decrease(int value) {
        return (int) (value * 0.7);
    }

}
