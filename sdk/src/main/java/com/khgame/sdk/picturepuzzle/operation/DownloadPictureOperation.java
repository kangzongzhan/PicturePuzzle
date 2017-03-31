package com.khgame.sdk.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.base.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zkang on 2017/2/26.
 */

public class DownloadPictureOperation extends Operation<File, Void> {

    private String uuid;
    private String url;

    public DownloadPictureOperation(String uuid, String url) {
        this.uuid = uuid;
        this.url = url;
    }

    @Override
    protected void doWork() {
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
        File localFile = Application.PictureFile(uuid);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            postSuccess(localFile);
        } catch (Exception e) {
            Log.w("DownLoadPicture", "fail, Exception:" + e.getMessage());
            postFailure(null);
        } finally {
            IoUtils.closeSilently(fileOutputStream);
        }
    }
}
