package com.khgame.picturepuzzle.serial;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialInstallEvent {

    public EventType type;
    public int value;

    public SerialInstallEvent(EventType type, int value) {
        this.type = type;
        this.value = value;
    }
    public SerialInstallEvent(EventType type) {
        this.type = type;
    }

    public enum EventType {
        BEGIN, INSTALLING, END
    }

}
