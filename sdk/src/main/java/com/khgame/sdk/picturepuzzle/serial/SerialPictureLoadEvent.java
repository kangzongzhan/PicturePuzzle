package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialPictureLoadEvent {
    public final Result result;
    public SerialPicture serialPicture;
    public SerialPictureLoadEvent (Result result) {
        this.result = result;
    }
}
