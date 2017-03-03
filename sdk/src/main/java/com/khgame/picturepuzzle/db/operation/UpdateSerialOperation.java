package com.khgame.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.picturepuzzle.db.table.SerialTable;
import com.khgame.picturepuzzle.model.Serial;

/**
 * Created by Kisha Deng on 3/2/2017.
 */

public class UpdateSerialOperation extends DBOperation<Void, Void> {

    private Serial serial;
    public UpdateSerialOperation(Serial serial) {
        this.serial = serial;
    }
    @Override
    protected void doWork() {
        ContentValues values = new ContentValues();
        values.put(SerialTable.Cols.GAMELEVEL, serial.gameLevel);
        int rows = db.update(SerialTable.NAME, values, SerialTable.Cols.UUID + "='" + serial.uuid + "'", null);
        if(rows == 0) {
            postFailure(null);
        } else {
            postSuccess(null);
        }
    }
}
