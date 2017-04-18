package com.khgame.sdk.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.sdk.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;

/**
 * Created by zkang on 2017/2/19.
 */

public class UpdateSerialPictureOperation extends DBOperation<SerialPicture, Void> {
    private SerialPicture serialPicture;

    public UpdateSerialPictureOperation(SerialPicture serialPicture) {
        this.serialPicture = serialPicture;
    }

    @Override
    protected void doWork() {
        ContentValues values = new ContentValues();
        values.put(SerialPictureTable.Cols.EASYDATA, serialPicture.easyData);
        values.put(SerialPictureTable.Cols.MEDIUMDATA, serialPicture.mediumData);
        values.put(SerialPictureTable.Cols.HARDDATA, serialPicture.hardData);
        int rows = db.update(SerialPictureTable.NAME, values, SerialPictureTable.Cols.UUID + "='" + serialPicture.uuid + "'", null);
        if (rows == 0) {
            postFailure(null);
        } else {
            postSuccess(null);
        }
    }
}
