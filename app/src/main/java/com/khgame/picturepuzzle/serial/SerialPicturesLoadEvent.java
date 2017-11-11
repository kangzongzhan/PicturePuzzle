package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.model.SerialPicture;

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
