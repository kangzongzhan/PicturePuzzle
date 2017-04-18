package com.khgame.sdk.picturepuzzle.db.operation;

import android.content.ContentValues;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/2/18.
 */

public class InsertClassicPictureOperation extends DBOperation<ClassicPicture, Void> {
    private static final String TAG = "InsertClassic";
    private ClassicPicture picture;

    public InsertClassicPictureOperation(ClassicPicture picture) {
        this.picture = picture;
    }

    @Override
    protected void doWork() {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, picture.uuid);
        values.put(Cols.EASYDATA, picture.easyData);
        values.put(Cols.MEDIUMDATA, picture.mediumData);
        values.put(Cols.HARDDATA, picture.hardData);

        long id = db.insert(ClassicPictureTable.NAME, null, values);

        if (id == -1) {
            postFailure(null);
            Log.e(TAG, "insert classic picture failure");
        } else {
            postSuccess(picture);
            Log.i(TAG, "insert classic success");
        }
    }
}
