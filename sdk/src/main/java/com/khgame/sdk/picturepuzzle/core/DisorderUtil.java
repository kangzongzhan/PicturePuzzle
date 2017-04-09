package com.khgame.sdk.picturepuzzle.core;

import android.support.annotation.NonNull;

import com.khgame.sdk.picturepuzzle.common.Constant;
import com.khgame.sdk.picturepuzzle.common.Probability;
import com.khgame.sdk.picturepuzzle.common.SettingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.khgame.sdk.picturepuzzle.core.GameLevel.getLevel;
import static com.khgame.sdk.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.sdk.picturepuzzle.core.GameLevel.yNums;

/**
 * Created by Kisha Deng on 2/12/2017.
 */

public class DisorderUtil {
    private static final String TAG = "DisorderUtil";
    private static SettingManager settings = SettingManager.Instance();
    public static String encode(List<Point> list) {
        int gameLevel = getLevel(list);
        int xNums = xNums(gameLevel);
        StringBuilder sb = new StringBuilder();
        for(Point point: list) {
            int index = point.y * xNums + point.x;
            char c = System64.encode(index);
            sb.append(c);
        }
        return sb.toString();
    }

    public static List<Point> decode(String str) {
        int gameLevel = getLevel(str);
        int xNums = xNums(gameLevel);
        List<Point> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        for(char c : chars) {
            int index = System64.decode(c);
            Point p = new Point();
            p.x = index % xNums;
            p.y = index / xNums;
            list.add(p);
        }
        return list;
    }

    public static String newDisorderString(int gameLevel) {
        return encode(newDisorderList(gameLevel));
    }

    public static List<Point> newDisorderList(int gameLevel) {
        String disorderBase = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                disorderBase = settings.getString(Constant.EASY_TEMPLATE_KEY, Constant.EASY_TEMPLATE_DEFAULT);
                break;
            case GameLevel.MEDIUM:
                disorderBase = settings.getString(Constant.MEDIUM_TEMPLATE_KEY, Constant.MEDIUM_TEMPLATE_DEFAULT);
                break;
            case GameLevel.HARD:
                disorderBase = settings.getString(Constant.HARD_TEMPLATE_KEY, Constant.HARD_TEMPLATE_DEFAULT);
        }


        final List<Point> list = randomStep(disorderBase, 1000);

        // set disorder base, be used for next disorder
        switch (gameLevel) {
            case GameLevel.EASY:
                settings.setString(Constant.EASY_TEMPLATE_KEY, encode(list));
                break;
            case GameLevel.MEDIUM:
                settings.setString(Constant.MEDIUM_TEMPLATE_KEY, encode(list));
                break;
            case GameLevel.HARD:
                settings.setString(Constant.HARD_TEMPLATE_KEY, encode(list));
        }

        return list;
    }

    public static List<Point> randomStep(String seed, int steps) {
        List<Point> seedList = decode(seed);
        return randomStep(seedList, steps);
    }

    public static List<Point> randomStep(List<Point> seed, int steps) {
        Probability probability = new Probability();
        probability.with(25, () -> swipUp(seed))
                .with(25, () -> swipDown(seed))
                .with(25, () -> swipLeft(seed))
                .with(25, () -> swipRight(seed));
        for(int i = 0; i < steps; i++) {
            probability.go();
        }
        return seed;
    }

    public static boolean swipUp(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex + xNums;

        if(targetIndex > list.size() - 1 || !valideIndex(list, targetIndex)) { //不可上划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public static boolean swipDown(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex - xNums;

        if(targetIndex < 0 || !valideIndex(list, targetIndex)) { //不可下划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public static boolean swipLeft(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex + 1;

        if(targetIndex % xNums == 0 || whiteIndex == list.size() - 1 || !valideIndex(list, targetIndex)) { //不可左划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public static boolean swipRight(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex - 1;

        if(targetIndex % xNums == xNums -1 || whiteIndex == list.size() - 1 || !valideIndex(list, targetIndex)) { //不可右划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    private static int locateWhitePoint(final List<Point> list) {
        Point whitePoint = new Point();
        whitePoint.x = 0;
        whitePoint.y = yNums(getLevel(list));
        return list.indexOf(whitePoint);
    }

    private static List<Point> cloneList(final List<Point> list) {
        List<Point> copyList = new ArrayList<>(list.size());
        for(Point p : list) {
            copyList.add(p.clone());
        }
        return copyList;
    }


    /**
     * 内复杂度
     * Point当前位置与其正确位置之间的距离
     * 计算方法
     * |x1 - x2| + |y1 - y2|
     */
    private static int innerDisorder(List<Point> list){
        int gameLevel = getLevel(list);
        int xNums = xNums(gameLevel);

        int sum = 0;
        for(Point point : list) {
            int index = list.indexOf(point);
            int x = index % xNums; // 当前x位置
            int y = index / xNums; // 当前y位置
            int value = Math.abs(x - point.x) + Math.abs(y - point.y);
            sum += value;
        }
        return sum;
    }

    /**
     * 外复杂度
     * 所以相邻的Point初始位置
     */
    private static int outterDisorder(List<Point> list){
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);
        final int yNums = yNums(gameLevel);

        int sum = 0;
        // #1 每个Point与右边Point距离
        for(int y = 0; y < yNums; y++) {
            for(int x = 0; x < xNums - 1; x++) {
                int index = y * xNums + x;
                Point left = list.get(index);
                Point right = list.get(index + 1);
                int value = Math.abs(left.x - right.x) + Math.abs(left.y - right.y);
                sum += value;
            }
        }

        // #2 每个Point与下面Point距离
        for(int y = 0; y < yNums -1; y++) {
            for(int x = 0; x < xNums; x++) {
                int index = y*xNums + x;
                Point up = list.get(index);
                Point down = list.get(index + xNums);
                int value = Math.abs(up.x - down.x) + Math.abs(up.y - down.y);
                sum += value;
            }
        }

        // 最后一行的Point与它上面那个
        Point last = list.get(list.size()-1);
        Point upOfLast = list.get(list.size() - 1 - xNums);
        int value = Math.abs(last.x - upOfLast.x) + Math.abs(last.y - upOfLast.y);
        sum += value;

        return sum;
    }

    /**
     * 按照 50% 30% 15% 5%的概率选择
     */
    private static List<Point> chooseList(List<Point> up, List<Point> right, List<Point> down, List<Point> left) {
        final List<DisorderValue> disorderValues = new ArrayList<>();
        disorderValues.add(new DisorderValue(up));
        disorderValues.add(new DisorderValue(right));
        disorderValues.add(new DisorderValue(down));
        disorderValues.add(new DisorderValue(left));
        Collections.sort(disorderValues);
        final List<Point>[] result = new ArrayList[1];
        Probability probability = new Probability();
        probability.with(50, () -> result[0] = disorderValues.get(0).list)
        .with(30, () -> result[0] = disorderValues.get(0).list)
        .with(15, () -> result[0] = disorderValues.get(0).list)
        .with(5, () -> result[0] = disorderValues.get(0).list);
        probability.go();
        return result[0];
    }

    private static boolean valideIndex(List<Point> list, int index) {
        return index < list.size() && index >= 0;
    }

    static class DisorderValue implements Comparable<DisorderValue>{
        int innerValue;
        int outterValue;
        List<Point> list;

        public DisorderValue(List<Point> list) {
            this.list = list;
            innerValue = innerDisorder(list);
            outterValue = outterDisorder(list);
        }
        @Override
        public int compareTo(@NonNull DisorderValue o) {
            if (innerValue + outterValue > o.innerValue + o.outterValue) {
                return -1;
            }
            if (innerValue + outterValue < o.innerValue + o.outterValue){
                return 1;
            }
            return 0;
        }
    }
}
