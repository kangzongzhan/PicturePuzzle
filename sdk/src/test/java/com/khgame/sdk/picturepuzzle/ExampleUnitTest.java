package com.khgame.sdk.picturepuzzle;

import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        Map<String, Boolean> map = new HashMap<>();
        assertEquals(false, map.get("1"));
    }


    @Test
    public void testDisorder() throws Exception {

        String easy = DisorderUtil.newDisorderString(GameLevel.EASY);
        String medium = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
        String hard = DisorderUtil.newDisorderString(GameLevel.HARD);

        System.out.print("easy:" + easy);
        System.out.print("medium:" + medium);
        System.out.print("hard:" + hard);

        assertEquals(4, 2 + 2);
    }
}