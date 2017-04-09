package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;

import java.util.List;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialPicturesLoadEvent {
    public final Result result;

    public String serialUuid;
    public List<SerialPicture> serialPictures;

    public SerialPicturesLoadEvent(Result result) {
        this.result = result;
    }
}
