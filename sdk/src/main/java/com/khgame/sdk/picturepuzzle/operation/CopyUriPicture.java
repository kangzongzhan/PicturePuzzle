package com.khgame.sdk.picturepuzzle.operation;

import android.net.Uri;

import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

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
        return classicPicture;
    }

}
