package com.khgame.picturepuzzle.core;

import java.util.ArrayList;
import java.util.List;

import static com.khgame.picturepuzzle.core.GameLevel.getLevel;
import static com.khgame.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.picturepuzzle.core.GameLevel.yNums;

/**
 * Created by Kisha Deng on 2/12/2017.
 */

public class DisorderUtil {

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

        // #1 新建list
        int xNums = xNums(gameLevel);
        int yNums = yNums(gameLevel);
        List<Point> list = new ArrayList();
        for(int y = 0; y < yNums; y++) {
            for(int x = 0; x < xNums; x++) {
                Point p = new Point();
                p.x = x;
                p.y = y;
                list.add(p);
            }
        }
        Point whitePoint = new Point();
        whitePoint.x = 0;
        whitePoint.y = yNums;
        list.add(whitePoint);

        // #2 打乱list
        while(true) {

            List<Point> listAfterUp = swipUp(list);
            List<Point> listAfterDown = swipDown(list);
            List<Point> listAfterRight = swipRight(list);
            List<Point> listAfterLeft = swipLeft(list);

            List<Point> bestDisorderList = listAfterUp;
            bestDisorderList = innerDisorder(bestDisorderList) + outterDisorder(bestDisorderList)
                    > innerDisorder(listAfterDown) + outterDisorder(listAfterDown)
                    ? bestDisorderList : listAfterDown;

            bestDisorderList = innerDisorder(bestDisorderList) + outterDisorder(bestDisorderList)
                    > innerDisorder(listAfterRight) + outterDisorder(listAfterRight)
                    ? bestDisorderList : listAfterRight;

            bestDisorderList = innerDisorder(bestDisorderList) + outterDisorder(bestDisorderList)
                    > innerDisorder(listAfterLeft) + outterDisorder(listAfterLeft)
                    ? bestDisorderList : listAfterLeft;

            list = bestDisorderList;
            if(innerDisorder(list) + outterDisorder(list) > 50){
                break;
            }
        }

        return list;
    }

    public static List<Point> swipUp(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        List<Point> copyList = cloneList(list);

        int whiteIndex = locateWhitePoint(copyList);
        int targetIndex = whiteIndex + xNums;

        if(targetIndex > list.size()-1) { //不可上划
            return copyList;
        }

        Point whitePoint = copyList.get(whiteIndex);
        Point targetPoint = copyList.remove(targetIndex);
        copyList.add(targetIndex, whitePoint);
        copyList.remove(whiteIndex);
        copyList.add(whiteIndex, targetPoint);

        return copyList;
    }

    public static List<Point> swipDown(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        List<Point> copyList = cloneList(list);

        int whiteIndex = locateWhitePoint(copyList);
        int targetIndex = whiteIndex - xNums;

        if(targetIndex < 0) { //不可下划
            return copyList;
        }

        Point whitePoint = copyList.get(whiteIndex);
        Point targetPoint = copyList.remove(targetIndex);
        copyList.add(targetIndex, whitePoint);
        copyList.remove(whiteIndex);
        copyList.add(whiteIndex, targetPoint);

        return copyList;
    }

    public static List<Point> swipLeft(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        List<Point> copyList = cloneList(list);

        int whiteIndex = locateWhitePoint(copyList);
        int targetIndex = whiteIndex + 1;

        if(targetIndex % xNums == 0 || whiteIndex == list.size() - 1) { //不可左划
            return copyList;
        }

        Point whitePoint = copyList.get(whiteIndex);
        Point targetPoint = copyList.remove(targetIndex);
        copyList.add(targetIndex, whitePoint);
        copyList.remove(whiteIndex);
        copyList.add(whiteIndex, targetPoint);

        return copyList;
    }

    public static List<Point> swipRight(final List<Point> list) {
        final int gameLevel = getLevel(list);
        final int xNums = xNums(gameLevel);

        List<Point> copyList = cloneList(list);

        int whiteIndex = locateWhitePoint(copyList);
        int targetIndex = whiteIndex - 1;

        if(targetIndex % xNums == xNums -1 || whiteIndex == list.size() - 1) { //不可右划
            return copyList;
        }

        Point whitePoint = copyList.get(whiteIndex);
        Point targetPoint = copyList.remove(targetIndex);
        copyList.add(targetIndex, whitePoint);
        copyList.remove(whiteIndex);
        copyList.add(whiteIndex, targetPoint);

        return copyList;
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

        return sum/2;
    }

}
