package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.model.Serial;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialInstallEvent {

    public EventType type;
    public Serial serial;
    public int progress;

    public SerialInstallEvent(EventType type) {
        this.type = type;
    }

    public enum EventType {
        BEGIN, INSTALLING, END
    }

}
