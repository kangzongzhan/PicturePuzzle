package com.khgame.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.picturepuzzle.model.SerialPicture;
import com.khgame.picturepuzzle.service.model.SerialPictureDto;

/**
 * Created by zkang on 2017/2/25.
 */

public class InsertSerialPictureOperation extends DBOperation<SerialPicture, Void> {

    private SerialPictureDto serialPictureDto;

    public InsertSerialPictureOperation(SerialPictureDto serialPictureDto) {
        this.serialPictureDto = serialPictureDto;
    }

    @Override
    protected void doWork() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(SerialPictureTable.Cols.UUID, serialPictureDto.uuid);
        contentValues.put(SerialPictureTable.Cols.NAME, serialPictureDto.name);
        contentValues.put(SerialPictureTable.Cols.SERIALUUID, serialPictureDto.serialUuid);
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
        serialPicture.serialUuid = serialPictureDto.serialUuid;
        serialPicture.networkPath = serialPictureDto.url;
        serialPicture.easyData = serialPictureDto.easyData;
        serialPicture.mediaData = serialPictureDto.mediumData;
        serialPicture.hardData = serialPictureDto.hardData;

        postSuccess(serialPicture);
    }
}
