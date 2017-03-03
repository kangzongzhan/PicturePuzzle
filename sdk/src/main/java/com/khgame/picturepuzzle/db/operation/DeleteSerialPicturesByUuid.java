package com.khgame.picturepuzzle.db.operation;

import com.khgame.picturepuzzle.db.table.SerialPictureTable;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public class DeleteSerialPicturesByUuid extends DBOperation<Void, Void> {

    private String uuid;

    public DeleteSerialPicturesByUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        int nums = db.delete(SerialPictureTable.NAME, SerialPictureTable.Cols.UUID + "='" + uuid + "'", null);
        if (nums == -1) {
           postFailure(null);
            return;
        }
        postSuccess(null);
    }
}
