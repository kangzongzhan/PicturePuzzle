package com.khgame.picturepuzzle.core;

/**
 * Created by Kisha Deng on 2/12/2017.
 * 以左上角原点 横向 x, 纵向 y
 */

public class Point {
    public int x;
    public int y;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return p.x == x && p.y == y;
        }
        return false;
    }

    @Override
    protected Point clone() {
        Point p = new Point(x, y);
        return p;
    }

    @Override
    public int hashCode() {
        return x * 10 + y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
