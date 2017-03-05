package com.khgame.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zkang on 2017/3/4.
 */

public class CopyUriPicture extends Operation<ClassicPicture, Void> {
    private Uri uri;

    public CopyUriPicture(Uri uri) {
        this.uri = uri;
    }

    @Override
    protected void doWork() {
        if(uri == null) {
            postFailure(null);
            return;
        }

        String uuid = uri.getLastPathSegment();
        ClassicPicture classicPicture = newClassicPicture(uuid);
        new InsertClassicPictureOperation(classicPicture).callback(new Callback<ClassicPicture, Void>() {
            @Override
            public void onSuccess(ClassicPicture classicPicture) {
                CopyUriPicture.this.postSuccess(classicPicture);
            }

            @Override
            public void onFailure(Void aVoid) {
                CopyUriPicture.this.postFailure(null);
            }
        }).execute();
    }

    private ClassicPicture newClassicPicture(String uuid) {
        ClassicPicture classicPicture = new ClassicPicture();
        classicPicture.uuid = uuid;
        classicPicture.easyData = DisorderUtil.newDisorderString(GameLevel.EASY);
        classicPicture.mediumData = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
        classicPicture.hardData = DisorderUtil.newDisorderString(GameLevel.HARD);
        classicPicture.networkPath = "";
        return classicPicture;
    }

}
