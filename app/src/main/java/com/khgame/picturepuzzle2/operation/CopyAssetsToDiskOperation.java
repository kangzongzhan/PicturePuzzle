package com.khgame.picturepuzzle2.operation;

import android.util.Log;

import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle2.App;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zkang on 2017/2/18.
 */

public class CopyAssetsToDiskOperation extends Operation<ClassicPicture, Void> {
    private static final String TAG = "CopyAssetsOperation";
    private String assets;
    private String uuid;

    public CopyAssetsToDiskOperation(String assets, String uuid) {
        this.assets = assets;
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = App.getInstance().getAssets().open(assets);
            outputStream = new FileOutputStream(App.PictureFile(uuid));
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

            ClassicPicture classicPicture = new ClassicPicture();
            classicPicture.uuid = uuid;
            postSuccess(classicPicture);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            postFailure(null);
        } finally {
            IoUtils.closeSilently(inputStream);
            IoUtils.closeSilently(outputStream);
        }
    }


}
