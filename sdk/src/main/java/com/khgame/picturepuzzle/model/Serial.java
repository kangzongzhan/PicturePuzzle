package com.khgame.picturepuzzle.model;

import android.os.Bundle;

/**
 * Created by zkang on 2017/2/24.
 */

public class Serial implements Comparable<Serial>{
    public String uuid;
    public String name;
    public State installState;
    public int installProgress;
    public int gameLevel;
    public String networkCoverPath;
    public String themeColor;

    @Override
    public int compareTo(Serial serial) {
        if (serial.installState == this.installState) {
            return name.compareTo(serial.name);
        }
        if (this.installState == State.INSTALLED) {
            return 1;
        }
        if (serial.installState == State.UNINSTALL) {
            return 1;
        }
        return name.compareTo(serial.name);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Serial) {
            Serial serial = (Serial) obj;
            return serial.uuid.equals(this.uuid);
        }
        return false;
    }

    public enum State {
        INSTALLED, INSTALLING, UNINSTALL
    }

}
