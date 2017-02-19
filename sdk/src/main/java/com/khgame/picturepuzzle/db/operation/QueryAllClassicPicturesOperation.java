package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.ClassicPicture;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable.Cols;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/2/18.
 */

public class QueryAllClassicPicturesOperation extends DBOperation<List, Void> {
    @Override
    protected void doWork() {

        String[] projection = {Cols.ID, Cols.ASSETSPATH, Cols.LOCALPATH, Cols.NETWORKPATH,
        Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        String orderBy = Cols.ID + " DESC";

        Cursor cursor = db.query(ClassicPictureTable.NAME, projection, null, null, null, null, orderBy);

        List<ClassicPicture> result = new ArrayList<>();

        if(cursor != null) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ClassicPicture picture = new ClassicPicture();
                picture._id = cursor.getLong(cursor.getColumnIndex(Cols.ID));
                picture.assetsPath = cursor.getString(cursor.getColumnIndex(Cols.ASSETSPATH));
                picture.localPath = cursor.getString(cursor.getColumnIndex(Cols.LOCALPATH));
                picture.networkPath = cursor.getString(cursor.getColumnIndex(Cols.NETWORKPATH));
                picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
                picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
                picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
                result.add(picture);
            }
            cursor.close();
            postSuccess(result);
        } else {
            postFailure(null);
        }
    }
}
