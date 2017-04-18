package com.khgame.sdk.picturepuzzle.events;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/4/9.
 */

public class ClassicPictureLoadEvent {

    public final Result result;
    public ClassicPicture classicPicture;

    public ClassicPictureLoadEvent(Result result) {
        this.result = result;
    }

}
