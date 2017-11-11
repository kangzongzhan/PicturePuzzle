package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.model.Serial;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialLoadEvent {
    public final Result result;
    public Serial serial;
    public SerialLoadEvent(Result result) {
        this.result = result;
    }
}
