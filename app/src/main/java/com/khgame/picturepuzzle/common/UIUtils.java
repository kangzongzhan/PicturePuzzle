package com.khgame.picturepuzzle.common;

import android.os.Build;

/**
 * Created by zkang on 2017/4/17.
 */

public class UIUtils {
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }
}
