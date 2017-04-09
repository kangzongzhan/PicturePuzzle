package com.khgame.sdk.picturepuzzle.db.operation;

import android.database.Cursor;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.db.model.ClassicPicturePo;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;

import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/2/18.
 */

public class QueryAllClassicPicturesOperation extends DBOperation<List<ClassicPicturePo>, Void> {
    private static final String TAG = "QueryAllClassicPictures";
    @Override
    protected void doWork() {

        String[] projection = {Cols.ID, Cols.UUID, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        String orderBy = Cols.ID + " DESC";

        Cursor cursor = db.query(ClassicPictureTable.NAME, projection, null, null, null, null, orderBy);

        List<ClassicPicturePo> result = new ArrayList<>();

        if(cursor != null) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ClassicPicturePo picture = new ClassicPicturePo();
                picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
                picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
                picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
                picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
                result.add(picture);
            }
            cursor.close();
            postSuccess(result);
            Log.i(TAG, "query all classic pictures success, count:" + result.size());
        } else {
            postFailure(null);
            Log.e(TAG, "query all classic pictures failure");
        }
    }
}
