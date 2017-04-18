package com.khgame.sdk.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.sdk.picturepuzzle.db.model.ClassicPicturePo;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable.Cols;

/**
 * Created by zkang on 2017/2/19.
 */

public class GetClassicPictureByUuidOperation extends DBOperation<ClassicPicturePo, Void> {
    private String uuid;
    public GetClassicPictureByUuidOperation(String uuid) {
        this.uuid = uuid;
    }
    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.UUID, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        String selection = Cols.UUID + "=?";
        String[] selectionArgs = {uuid};
        Cursor cursor = db.query(ClassicPictureTable.NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClassicPicturePo picture = new ClassicPicturePo();
                picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
                picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
                picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
                picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
                postSuccess(picture);
                cursor.close();
                return;
            }
        }
        postFailure(null);
    }
}
