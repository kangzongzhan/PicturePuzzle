package com.khgame.sdk.picturepuzzle.events;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

import java.util.List;

/**
 * Created by zkang on 2017/4/9.
 */

public class ClassicPicturesLoadEvent {

    public final Result result;
    public List<ClassicPicture> classicPictures;

    public ClassicPicturesLoadEvent(Result result) {
        this.result = result;
    }
}
