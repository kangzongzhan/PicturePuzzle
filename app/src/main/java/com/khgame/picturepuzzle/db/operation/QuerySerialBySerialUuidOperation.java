package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.SerialPo;
import com.khgame.picturepuzzle.db.table.SerialTable;

/**
 * Created by zkang on 2017/4/8.
 */

public class QuerySerialBySerialUuidOperation extends DBOperation<SerialPo, Void> {
    private String serialUuid;
    public QuerySerialBySerialUuidOperation(String serialUuid) {
        this.serialUuid = serialUuid;
    }
    @Override
    protected void doWork() {
        String[] columns = {SerialTable.Cols.UUID, SerialTable.Cols.NAME, SerialTable.Cols.GAMELEVEL, SerialTable.Cols.PRIMARYCOLOR, SerialTable.Cols.SECONDARYCOLOR};
        String selection = SerialTable.Cols.UUID + "=?";
        String[] selectionArgs = {serialUuid};
        String orderBy = SerialTable.Cols.ID + " DESC";
        Cursor cursor = db.query(SerialTable.NAME, columns, selection, selectionArgs, null, null, orderBy);
        SerialPo serialPo = new SerialPo();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                serialPo.uuid = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.UUID));
                serialPo.name = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.NAME));
                serialPo.gameLevel = cursor.getInt(cursor.getColumnIndex(SerialTable.Cols.GAMELEVEL));
                serialPo.primaryColor = cursor.getInt(cursor.getColumnIndex(SerialTable.Cols.PRIMARYCOLOR));
                serialPo.secondaryColor = cursor.getInt(cursor.getColumnIndex(SerialTable.Cols.SECONDARYCOLOR));
            }
            cursor.close();
            postSuccess(serialPo);
            return;
        }
        postFailure(null);
    }
}
