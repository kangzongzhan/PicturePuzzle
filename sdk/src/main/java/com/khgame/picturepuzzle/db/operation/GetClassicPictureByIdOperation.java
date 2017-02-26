package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable.Cols;
import com.khgame.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/2/19.
 */

public class GetClassicPictureByIdOperation extends DBOperation<ClassicPicture, Void>{
    private long id;
    public GetClassicPictureByIdOperation(long id) {
        this.id = id;
    }
    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.UUID, Cols.NETWORKPATH, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        Cursor cursor = db.query(ClassicPictureTable.NAME, columns, Cols.ID + "=" + id, null, null, null, null);
        if(cursor == null) {
            postFailure(null);
        }
        cursor.moveToFirst();
        ClassicPicture picture = new ClassicPicture();
        picture._id = cursor.getLong(cursor.getColumnIndex(Cols.ID));
        picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
        picture.networkPath = cursor.getString(cursor.getColumnIndex(Cols.NETWORKPATH));
        picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
        picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
        picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
        postSuccess(picture);
        cursor.close();
    }
}
