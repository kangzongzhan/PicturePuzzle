package com.khgame.picturepuzzle.core;

import java.util.HashMap;

/**
 * Created by Kisha Deng on 2/12/2017.
 */

public class System64 {

    private static char[] metaData = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static HashMap<Integer, Character> encodeMap = new HashMap();
    private static HashMap<Character, Integer> decodeMap = new HashMap();

    static {
        for (int i = 0 ; i < 64; i++) {
            encodeMap.put(i, metaData[i]);
            decodeMap.put(metaData[i], i);
        }
    }

    public static char encode(int n) {
        char c = encodeMap.get(n);
        return  c;
    }

    public static int decode(char c) {
        int n = decodeMap.get(c);
        return n;
    }
}
