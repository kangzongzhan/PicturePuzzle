package com.khgame.picturepuzzle.db.operation;

import android.database.Cursor;

import com.khgame.picturepuzzle.db.model.ClassicPicture;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable.Cols;

/**
 * Created by zkang on 2017/2/19.
 */

public class GetClassicPictureByIdOperation extends DBOperation<ClassicPicture, Void>{
    private long id;
    public GetClassicPictureByIdOperation(long id) {
        this.id = id;
    }
    @Override
    protected void doWork() {
        String[] columns = {Cols.ID, Cols.ASSETSPATH, Cols.LOCALPATH, Cols.NETWORKPATH,
        Cols.EASYDATA, Cols.MEDIUMDATA, Cols.HARDDATA};
        Cursor cursor = db.query(ClassicPictureTable.NAME, columns, Cols.ID + "=" + id, null,null,null,null);
        if(cursor == null) {
            postFailure(null);
        }
        cursor.moveToFirst();
        ClassicPicture picture = new ClassicPicture();
        picture._id = cursor.getLong(cursor.getColumnIndex(Cols.ID));
        picture.assetsPath = cursor.getString(cursor.getColumnIndex(Cols.ASSETSPATH));
        picture.localPath = cursor.getString(cursor.getColumnIndex(Cols.LOCALPATH));
        picture.networkPath = cursor.getString(cursor.getColumnIndex(Cols.NETWORKPATH));
        picture.easyData = cursor.getString(cursor.getColumnIndex(Cols.EASYDATA));
        picture.mediumData = cursor.getString(cursor.getColumnIndex(Cols.MEDIUMDATA));
        picture.hardData = cursor.getString(cursor.getColumnIndex(Cols.HARDDATA));
        postSuccess(picture);
        cursor.close();
    }
}
