package com.khgame.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.common.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zkang on 2017/2/25.
 */

public class LoadPictureOperation extends Operation<Bitmap, Void> {

    private String uuid;

    public LoadPictureOperation(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {

        // load bitmap from disk
        File localFile = Application.PictureFile(uuid);
        if (localFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
            if (bitmap != null) {
                postSuccess(bitmap);
                return;
            }
        }

        // load bitmap from network
        String url = Constant.PICTURE_URL_BASE + uuid;
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
        if (bitmap == null) {
            postFailure(null);
        }
        postSuccess(bitmap);

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
