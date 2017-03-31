package com.khgame.sdk.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.base.Application;
import com.khgame.sdk.picturepuzzle.model.BitmapEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zkang on 2017/2/25.
 */

public class LoadPictureOperation extends Operation<BitmapEntry, Void> {

    private String uuid;
    private String url;

    public LoadPictureOperation(String uuid, String url) {
        this.uuid = uuid;
        this.url = url;
    }

    @Override
    protected void doWork() {

        // load bitmap from disk
        File localFile = Application.PictureFile(uuid);
        if(localFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
            if(bitmap != null) {
                BitmapEntry entry = new BitmapEntry();
                entry.uuid = uuid;
                entry.bitmap = bitmap;
                postSuccess(entry);
                return;
            }
        }

        // load bitmap from network
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
        if(bitmap == null) {
            postFailure(null);
        }
        BitmapEntry entry = new BitmapEntry();
        entry.uuid = uuid;
        entry.bitmap = bitmap;
        postSuccess(entry);
        postSuccess(entry);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            Log.w("LoadPictureOperation", "same picture fail");
        } finally {
            IoUtils.closeSilently(fileOutputStream);
        }
    }
}