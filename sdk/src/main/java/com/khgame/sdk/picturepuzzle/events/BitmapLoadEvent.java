package com.khgame.sdk.picturepuzzle.events;

import android.graphics.Bitmap;

import com.khgame.sdk.picturepuzzle.common.Result;

/**
 * Created by zkang on 2017/4/8.
 */

public class BitmapLoadEvent {
    public final Result result;

    public String uuid;
    public Bitmap bitmap;

    public BitmapLoadEvent(Result result) {
        this.result = result;
    }
}
