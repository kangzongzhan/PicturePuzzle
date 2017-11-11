package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.model.SerialPicture;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialPictureLoadEvent {
    public final Result result;
    public SerialPicture serialPicture;
    public SerialPictureLoadEvent(Result result) {
        this.result = result;
    }
}
