package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.model.Serial;

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
