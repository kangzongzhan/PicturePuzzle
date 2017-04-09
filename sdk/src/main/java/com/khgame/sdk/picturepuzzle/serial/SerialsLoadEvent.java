package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.Serial;

import java.util.List;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialsLoadEvent {

    public final Result result;
    public List<Serial> serials;

    public SerialsLoadEvent(Result result) {
        this.result = result;
    }
}
