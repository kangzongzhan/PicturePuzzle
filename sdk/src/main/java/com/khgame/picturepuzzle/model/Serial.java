package com.khgame.picturepuzzle.model;

import android.os.Bundle;

/**
 * Created by zkang on 2017/2/24.
 */

public class Serial implements Comparable<Serial>{
    public String uuid;
    public String name;
    public String installed;
    public int gameLevel;
    public String networkCoverPath;
    public String themeColor;

    @Override
    public int compareTo(Serial o) {
        if(o.installed.equals(this.installed)) {
            return name.compareTo(o.name);
        }
        if(this.installed.equals(SerialState.INSTALLED) || o.installed.equals(SerialState.UNINSTALL)) {
            return 1;
        }
        return -1;
    }

    public static class SerialState {
        public static final String INSTALLED = "INSTALLED";
        public static final String INSTALLING = "INSTALLING";
        public static final String UNINSTALL = "UNINSTALL";
    }

}
