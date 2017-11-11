package com.khgame.picturepuzzle.db.operation;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable;

/**
 * Created by zkang on 2017/3/17.
 */

public class DeleteClassicPictureByUuid extends DBOperation<Void, Void> {

    private String uuid;
    public DeleteClassicPictureByUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        int nums = db.delete(ClassicPictureTable.NAME, ClassicPictureTable.Cols.UUID + "='" + uuid + "'", null);
        if (nums == -1) {
            postFailure(null);
            return;
        }
        postSuccess(null);
    }
}
