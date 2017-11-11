package com.khgame.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.picturepuzzle.model.ClassicPicture;

/**
 * Created by zkang on 2017/2/19.
 */

public class UpdateClassicPictureOperation extends DBOperation<Void, Void> {
    private ClassicPicture classicPicture;
    public UpdateClassicPictureOperation(ClassicPicture classicPicture) {
        this.classicPicture = classicPicture;
    }

    @Override
    protected void doWork() {
        ContentValues values = new ContentValues();
        values.put(ClassicPictureTable.Cols.EASYDATA, classicPicture.easyData);
        values.put(ClassicPictureTable.Cols.MEDIUMDATA, classicPicture.mediumData);
        values.put(ClassicPictureTable.Cols.HARDDATA, classicPicture.hardData);
        int rows = db.update(ClassicPictureTable.NAME, values, ClassicPictureTable.Cols.UUID + "='" + classicPicture.uuid + "'", null);
        if (rows == 0) {
            postFailure(null);
        } else {
            postSuccess(null);
        }
    }
}
