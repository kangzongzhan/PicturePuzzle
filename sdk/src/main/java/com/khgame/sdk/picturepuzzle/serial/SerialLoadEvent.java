package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.Serial;

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
