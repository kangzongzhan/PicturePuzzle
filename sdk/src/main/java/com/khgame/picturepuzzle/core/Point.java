package com.khgame.picturepuzzle.core;

/**
 * Created by Kisha Deng on 2/12/2017.
 * 以左上角原点 横向 x, 纵向 y
 */

public class Point {
    int x;
    int y;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point) {
            Point p = (Point) obj;
            return p.x == x && p.y == y;
        }
        return false;
    }

    @Override
    protected Point clone() {
        Point p = new Point();
        p.x = x;
        p.y = y;
        return p;
    }

    @Override
    public String toString() {
        return "[x:" + x + ", y:" + y + "]";
    }
}
