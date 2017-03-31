package com.khgame.sdk.picturepuzzle.db.operation;

import android.content.ContentValues;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/2/18.
 */

public class InsertClassicPictureOperation extends DBOperation<ClassicPicture, Void>{
    private static final String TAG = "InsertClassic";
    private ClassicPicture picture;

    public InsertClassicPictureOperation(ClassicPicture picture) {
        this.picture = picture;
    }

    @Override
    protected void doWork() {
        try {
            ContentValues values = new ContentValues();
            values.put(Cols.UUID, picture.uuid);
            values.put(Cols.NETWORKPATH, picture.networkPath);
            values.put(Cols.EASYDATA, picture.easyData);
            values.put(Cols.MEDIUMDATA, picture.mediumData);
            values.put(Cols.HARDDATA, picture.hardData);

            long id = db.insert(ClassicPictureTable.NAME, null, values);

            if(id == -1) {
                postFailure(null);
            } else {
                postSuccess(picture);
            }
        } catch (Exception e) {
            Log.e(TAG, "insert failure", e);
            postFailure(null);
        }
    }
}
