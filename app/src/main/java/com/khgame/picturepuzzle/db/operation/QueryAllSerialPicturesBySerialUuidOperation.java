package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.SerialPicturePo;
import com.khgame.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.picturepuzzle.db.table.SerialPictureTable.Cols;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/2/25.
 */

public class QueryAllSerialPicturesBySerialUuidOperation extends DBOperation<List<SerialPicturePo>, Void> {

    private String uuid;

    public QueryAllSerialPicturesBySerialUuidOperation(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.UUID, Cols.NAME, Cols.SERIALUUID,
                Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA, Cols.URL};
        Cursor cursor = db.query(SerialPictureTable.NAME, columns, Cols.SERIALUUID + "='" + uuid + "'", null, null, null, null);
        if (cursor == null) {
            postFailure(null);
            return;
        }

        List<SerialPicturePo> serialPicturePos = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            SerialPicturePo serialPicturePo = new SerialPicturePo();
            serialPicturePo.id = cursor.getLong(cursor.getColumnIndex(Cols.ID));
            serialPicturePo.name = cursor.getString(cursor.getColumnIndex(Cols.NAME));
            serialPicturePo.uuid = cursor.getString(cursor.getColumnIndex(Cols.UUID));
            serialPicturePo.serialUuid = cursor.getString(cursor.getColumnIndex(Cols.SERIALUUID));
            serialPicturePo.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
            serialPicturePo.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
            serialPicturePo.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
            serialPicturePos.add(serialPicturePo);
        }
        postSuccess(serialPicturePos);
        cursor.close();
    }
}
