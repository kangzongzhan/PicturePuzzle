package com.khgame.picturepuzzle;

import android.graphics.Color;

import com.khgame.picturepuzzle.common.ColorUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zkang on 2017/4/9.
 */

public class ColorUtilTest {

    @Test
    public void darkerTest() throws Exception {
        int color = Color.rgb(100, 100, 100);
        int afterDarker = ColorUtil.darker(color);
        assertEquals(afterDarker, Color.rgb(70, 70, 70));
    }

}
