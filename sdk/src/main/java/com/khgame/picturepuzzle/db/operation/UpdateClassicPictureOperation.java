package com.khgame.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.model.SerialPicture;

import java.util.List;

/**
 * Created by zkang on 2017/2/19.
 */

public class UpdateClassicPictureOperation extends DBOperation<SerialPicture, Void> {
    private String uuid;
    private List<Point> gameData;

    public UpdateClassicPictureOperation(String  uuid, List<Point> gameData) {
        this.uuid = uuid;
        this.gameData = gameData;
    }

    @Override
    protected void doWork() {
        int gameLevel = GameLevel.getLevel(gameData);
        String col = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                col = ClassicPictureTable.Cols.EASYDATA;
                break;
            case GameLevel.MEDIUM:
                col = ClassicPictureTable.Cols.MEDIUMDATA;
                break;
            case GameLevel.HARD:
                col = ClassicPictureTable.Cols.HARDDATA;
                break;
        }

        ContentValues values = new ContentValues();
        values.put(col, DisorderUtil.encode(gameData));
        int rows = db.update(ClassicPictureTable.NAME, values, ClassicPictureTable.Cols.UUID + "='" + uuid + "'", null);
        if(rows == 0) {
            postFailure(null);
        } else {
            postSuccess(null);
        }
    }
}
