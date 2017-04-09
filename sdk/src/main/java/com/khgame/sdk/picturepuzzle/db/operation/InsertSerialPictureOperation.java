package com.khgame.sdk.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.sdk.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.SerialTable;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;
import com.khgame.sdk.picturepuzzle.service.model.SerialPictureDto;

/**
 * Created by zkang on 2017/2/25.
 */

public class InsertSerialPictureOperation extends DBOperation<SerialPicture, Void> {

    private SerialPictureDto serialPictureDto;
    private String serialUuid;

    public InsertSerialPictureOperation(SerialPictureDto serialPictureDto, String serialUuid) {
        this.serialPictureDto = serialPictureDto;
        this.serialUuid = serialUuid;
    }

    @Override
    protected void doWork() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(SerialPictureTable.Cols.UUID, serialPictureDto.uuid);
        contentValues.put(SerialPictureTable.Cols.NAME, serialPictureDto.name);
        contentValues.put(SerialPictureTable.Cols.SERIALUUID, serialUuid);
        contentValues.put(SerialPictureTable.Cols.URL, serialPictureDto.url);
        contentValues.put(SerialPictureTable.Cols.EASYDATA, serialPictureDto.easyData);
        contentValues.put(SerialPictureTable.Cols.MEDIUMDATA, serialPictureDto.mediumData);
        contentValues.put(SerialPictureTable.Cols.HARDDATA, serialPictureDto.hardData);

        long id = db.insert(SerialPictureTable.NAME, null, contentValues);

        if (id == -1) {
            postFailure(null);
            return;
        }

        SerialPicture serialPicture = new SerialPicture();
        serialPicture.uuid = serialPictureDto.uuid;
        serialPicture.name = serialPictureDto.name;
        serialPicture.serialUuid = serialUuid;
        serialPicture.easyData = serialPictureDto.easyData;
        serialPicture.mediumData = serialPictureDto.mediumData;
        serialPicture.hardData = serialPictureDto.hardData;

        postSuccess(serialPicture);
    }
}
