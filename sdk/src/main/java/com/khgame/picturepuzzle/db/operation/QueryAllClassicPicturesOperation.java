package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.picturepuzzle.model.ClassicPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/2/18.
 */

public class QueryAllClassicPicturesOperation extends DBOperation<List, Void> {
    @Override
    protected void doWork() {

        String[] projection = {Cols.ID, Cols.UUID, Cols.NETWORKPATH, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        String orderBy = Cols.ID + " DESC";

        Cursor cursor = db.query(ClassicPictureTable.NAME, projection, null, null, null, null, orderBy);

        List<ClassicPicture> result = new ArrayList<>();

        if(cursor != null) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ClassicPicture picture = new ClassicPicture();
                picture._id = cursor.getLong(cursor.getColumnIndex(Cols.ID));
                picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
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
