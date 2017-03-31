package com.khgame.sdk.picturepuzzle.operation;

import com.khgame.sdk.picturepuzzle.base.Application;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.FileOutputStream;
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
            inputStream = Application.getInstance().getAssets().open(assets);
            outputStream = new FileOutputStream(Application.PictureFile(uuid));
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

            ClassicPicture classicPicture = new ClassicPicture();
            classicPicture.uuid = uuid;
            postSuccess(classicPicture);
        } catch (Exception e) {
            postFailure(null);
        } finally {
            IoUtils.closeSilently(inputStream);
            IoUtils.closeSilently(outputStream);
        }
    }


}
