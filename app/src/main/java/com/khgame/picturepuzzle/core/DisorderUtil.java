package com.khgame.picturepuzzle.core;

import android.support.annotation.NonNull;
import com.jakewharton.rxrelay2.PublishRelay;
import com.khgame.picturepuzzle.common.Probability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

import static com.khgame.picturepuzzle.core.GameLevel.getLevel;
import static com.khgame.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.picturepuzzle.core.GameLevel.yNums;

public class DisorderUtil {
    private static final String TAG = "DisorderUtil";
    private String EASY_TEMPLATE;
    private String MEDIUM_TEMPLATE;
    private String HARD_TEMPLATE;

    private PublishRelay<String> templatePublish = PublishRelay.create();

    public DisorderUtil(String easy, String medium, String hard) {
        this.EASY_TEMPLATE = easy;
        this.MEDIUM_TEMPLATE = medium;
        this.HARD_TEMPLATE = hard;
    }

    public String encode(List<Point> list) {
        int gameLevel = getLevel(list);
        int xNums = xNums(gameLevel);
        StringBuilder sb = new StringBuilder();
        for (Point point: list) {
            int index = point.y * xNums + point.x;
            char c = System64.encode(index);
            sb.append(c);
        }
        return sb.toString();
    }

    public List<Point> decode(String str) {
        int gameLevel = getLevel(str);
        int xNums = xNums(gameLevel);
        List<Point> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            int index = System64.decode(c);
            Point p = new Point();
            p.x = index % xNums;
            p.y = index / xNums;
            list.add(p);
        }
        return list;
    }

    public String newDisorderString(int gameLevel) {
        return encode(newDisorderList(gameLevel));
    }

    public List<Point> newDisorderList(int gameLevel) {
        String disorderBase = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                disorderBase = EASY_TEMPLATE;
                break;
            case GameLevel.MEDIUM:
                disorderBase = MEDIUM_TEMPLATE;
                break;
            case GameLevel.HARD:
                disorderBase = HARD_TEMPLATE;
        }


        final List<Point> list = randomStep(disorderBase, 1000);

        // set disorder base, be used for next disorder
        String base = encode(list);
        switch (gameLevel) {
            case GameLevel.EASY:
                EASY_TEMPLATE = base;
                break;
            case GameLevel.MEDIUM:
                MEDIUM_TEMPLATE = base;
                break;
            case GameLevel.HARD:
                HARD_TEMPLATE = base;
        }
        templatePublish.accept(base);
        return list;
    }

    public Observable<String> observeTemplate() {
        return templatePublish;
    }

    public List<Point> randomStep(String seed, int steps) {
        List<Point> seedList = decode(seed);
        return randomStep(seedList, steps);
    }

    public List<Point> randomStep(List<Point> seed, int steps) {
        Probability probability = new Probability();
        probability.with(25, () -> swipUp(seed))
                .with(25, () -> swipDown(seed))
                .with(25, () -> swipLeft(seed))
                .with(25, () -> swipRight(seed));
        for (int i = 0; i < steps; i++) {
            probability.go();
        }
        return seed;
    }

    public boolean swipUp(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex + xNums;

        if (targetIndex > list.size() - 1 || !valideIndex(list, targetIndex)) { //不可上划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public boolean swipDown(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex - xNums;

        if (targetIndex < 0 || !valideIndex(list, targetIndex)) { //不可下划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public boolean swipLeft(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex + 1;

        if (targetIndex % xNums == 0 || whiteIndex == list.size() - 1 || !valideIndex(list, targetIndex)) { //不可左划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    public boolean swipRight(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        int whiteIndex = locateWhitePoint(list);
        int targetIndex = whiteIndex - 1;

        if (targetIndex % xNums == xNums - 1 || whiteIndex == list.size() - 1 || !valideIndex(list, targetIndex)) { //不可右划
            return false;
        }

        Point whitePoint = list.get(whiteIndex);
        Point targetPoint = list.remove(targetIndex);
        list.add(targetIndex, whitePoint);
        list.remove(whiteIndex);
        list.add(whiteIndex, targetPoint);

        return true;
    }

    private int locateWhitePoint(final List<Point> list) {
        Point whitePoint = new Point();
        whitePoint.x = 0;
        whitePoint.y = yNums(getLevel(list));
        return list.indexOf(whitePoint);
    }

    private List<Point> cloneList(final List<Point> list) {
        List<Point> copyList = new ArrayList<>(list.size());
        for (Point p : list) {
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
    private int innerDisorder(List<Point> list) {
        int gameLevel = getLevel(list);
        int xNums = xNums(gameLevel);

        int sum = 0;
        for (Point point : list) {
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
    private int outterDisorder(List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);
        final int yNums = yNums(gameLevel);

        int sum = 0;
        // #1 每个Point与右边Point距离
        for (int y = 0; y < yNums; y++) {
            for (int x = 0; x < xNums - 1; x++) {
                int index = y * xNums + x;
                Point left = list.get(index);
                Point right = list.get(index + 1);
                int value = Math.abs(left.x - right.x) + Math.abs(left.y - right.y);
                sum += value;
            }
        }

        // #2 每个Point与下面Point距离
        for (int y = 0; y < yNums - 1; y++) {
            for (int x = 0; x < xNums; x++) {
                int index = y * xNums + x;
                Point up = list.get(index);
                Point down = list.get(index + xNums);
                int value = Math.abs(up.x - down.x) + Math.abs(up.y - down.y);
                sum += value;
            }
        }

        // 最后一行的Point与它上面那个
        Point last = list.get(list.size() - 1);
        Point upOfLast = list.get(list.size() - 1 - xNums);
        int value = Math.abs(last.x - upOfLast.x) + Math.abs(last.y - upOfLast.y);
        sum += value;

        return sum;
    }

    /**
     * 按照 50% 30% 15% 5%的概率选择
     */
    private List<Point> chooseList(List<Point> up, List<Point> right, List<Point> down, List<Point> left) {
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

    class DisorderValue implements Comparable<DisorderValue> {
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
            if (innerValue + outterValue < o.innerValue + o.outterValue) {
                return 1;
            }
            return 0;
        }
    }
}
