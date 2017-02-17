package com.khgame.picturepuzzle.core;

import java.util.List;

/**
 * Created by Kisha Deng on 2/12/2017.
 */

public class GameLevel {

    public static final int EASY = 1; // 3*4
    public static final int MEDIUM = 2; // 4*6
    public static final int HARD = 3; // 6*8

    public static int getLevel(List<Point> list) {
        return _getLevel(list.size());
    }

    public static int getLevel(String str){
        return _getLevel(str.length());
    }

    private static int _getLevel(int size) {
        switch (size) {
            case 13: //3*4+1
                return EASY;
            case 25: // 4*6+1
                return MEDIUM;
            case 49: // 6*8+1
                return HARD;
            default:
                return EASY;
        }
    }

    public static int xNums(int gameLevel) {
        switch (gameLevel){
            case EASY:
                return 3;
            case MEDIUM:
                return 4;
            case HARD:
                return 6;
            default:return 3;
        }
    }
    public static int yNums(int gameLevel) {
        switch (gameLevel){
            case EASY:
                return 4;
            case MEDIUM:
                return 6;
            case HARD:
                return 8;
            default:return 4;
        }
    }
}
