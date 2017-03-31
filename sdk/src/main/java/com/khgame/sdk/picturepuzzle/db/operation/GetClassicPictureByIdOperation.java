package com.khgame.sdk.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/2/19.
 */

public class GetClassicPictureByIdOperation extends DBOperation<ClassicPicture, Void>{
    private String uuid;
    public GetClassicPictureByIdOperation(String uuid) {
        this.uuid = uuid;
    }
    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.UUID, Cols.NETWORKPATH, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        Cursor cursor = db.query(ClassicPictureTable.NAME, columns, Cols.UUID + "='" + uuid + "'", null, null, null, null);
        if(cursor == null) {
            postFailure(null);
        }
        cursor.moveToFirst();
        ClassicPicture picture = new ClassicPicture();
        picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
        picture.networkPath = cursor.getString(cursor.getColumnIndex(Cols.NETWORKPATH));
        picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
        picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
        picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
        postSuccess(picture);
        cursor.close();
    }
}
