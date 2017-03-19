package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.model.Serial;

/**
 * Created by zkang on 2017/3/17.
 */

public class SerialStateUpdateEvent {
    public Serial serial;
    public SerialStateUpdateEvent(Serial serial) {
        this.serial = serial;
    }
}
