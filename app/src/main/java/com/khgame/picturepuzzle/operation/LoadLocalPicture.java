package com.khgame.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.khgame.sdk.picturepuzzle.operation.Operation;

/**
 * Created by zkang on 2017/2/19.
 */

public class LoadLocalPicture extends Operation<Bitmap, String> {

    private String localPath;
    public LoadLocalPicture(String localPath) {
        this.localPath = localPath;
    }
    @Override
    protected void doWork() {
        Bitmap bitmap = BitmapFactory.decodeFile(localPath);
        if(bitmap == null) {
            postFailure("");
        } else {
            postSuccess(bitmap);
        }
    }

}
