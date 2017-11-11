package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.SerialPicturePo;
import com.khgame.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.picturepuzzle.db.table.SerialPictureTable.Cols;

/**
 * Created by zkang on 2017/4/8.
 */

public class GetSerialPictureByUuidOperation extends DBOperation<SerialPicturePo, Void> {
    private String uuid;

    public GetSerialPictureByUuidOperation(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.UUID, Cols.SERIALUUID, Cols.NAME, Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        String selection = Cols.UUID + "=?";
        String[] selectionArgs = {uuid};
        Cursor cursor = db.query(SerialPictureTable.NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                SerialPicturePo picture = new SerialPicturePo();
                picture.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
                picture.serialUuid = cursor.getString(cursor.getColumnIndex(Cols.SERIALUUID));
                picture.name = cursor.getString(cursor.getColumnIndex(Cols.NAME));
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
