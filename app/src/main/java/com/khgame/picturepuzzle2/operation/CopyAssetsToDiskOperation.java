package com.khgame.picturepuzzle2.operation;

import android.util.Log;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle2.App;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zkang on 2017/2/18.
 */

public class CopyAssetsToDiskOperation extends Operation<File, Void> {
    private static final String TAG = "CopyAssetsOperation";
    private String assets;
    private File destination;

    public CopyAssetsToDiskOperation(String assets, File destination) {
        this.assets = assets;
        this.destination = destination;
    }

    @Override
    protected void doWork() {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = App.application.getAssets().open(assets);
            outputStream = new FileOutputStream(destination);
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            postSuccess(destination);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            postFailure(null);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    private void closeQuietly(Closeable closeable) {
        if(closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
