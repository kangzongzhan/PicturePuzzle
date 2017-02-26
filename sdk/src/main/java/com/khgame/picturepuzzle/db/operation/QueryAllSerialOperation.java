package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.SerialPo;
import com.khgame.picturepuzzle.db.table.SerialTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/2/25.
 * 获取全部数据库数据
 */

public class QueryAllSerialOperation extends DBOperation<List<SerialPo>, Void> {
    @Override
    protected void doWork() {
        String[] projection = {SerialTable.Cols.UUID, SerialTable.Cols.NAME, SerialTable.Cols.GAMELEVEL, SerialTable.Cols.NETWORKCOVERPATH, SerialTable.Cols.THEMECOLOR};
        String orderBy = SerialTable.Cols.ID + " DESC";
        Cursor cursor = db.query(SerialTable.NAME, projection, null, null, null, null, orderBy);

        List<SerialPo> list = new ArrayList<>();
        if(cursor != null) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                SerialPo serialPo = new SerialPo();
                serialPo.uuid = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.UUID));
                serialPo.name = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.NAME));
                serialPo.gameLevel = cursor.getInt(cursor.getColumnIndex(SerialTable.Cols.GAMELEVEL));
                serialPo.networkCoverPath = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.NETWORKCOVERPATH));
                serialPo.themeColor = cursor.getString(cursor.getColumnIndex(SerialTable.Cols.THEMECOLOR));
                list.add(serialPo);
            }
            cursor.close();
            postSuccess(list);
            return;
        }
        postFailure(null);
    }
}
